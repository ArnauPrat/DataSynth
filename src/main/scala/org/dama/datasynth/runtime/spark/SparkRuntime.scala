package org.dama.datasynth.runtime.spark

import java.net.{URL, URLClassLoader}

import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.sql.{Dataset, SparkSession}
import org.dama.datasynth.DataSynthConfig
import org.dama.datasynth.common.utils.FileUtils
import org.dama.datasynth.executionplan.ExecutionPlan
import org.dama.datasynth.runtime.spark.operators._
import org.dama.datasynth.runtime.spark.passes.{InjectRuntimeGenerators, RuntimePropertyGeneratorBuilder}

import scala.collection.mutable


/**
  * Created by aprat on 6/04/17.
  */
object SparkRuntime{

  private[spark] val edgeTableOperator = new EdgeTableOperator()
  private[spark] val fetchTableOperator = new FetchTableOperator()
  private[spark] val evalValueOperator = new EvalValueOperator()
  private[spark] val fetchRndGeneratorOperator = new FetchRndGeneratorOperator()
  private[spark] val instantiatePropertyGeneratorOperator  = new InstantiatePropertyGeneratorOperator()
  private[spark] val instantiateStructureGeneratorOperator = new InstantiateStructureGeneratorOperator()
  private[spark] val propertyTableOperator = new PropertyTableOperator()
  private[spark] val tableSizeOperator = new TableSizeOperator()

  var config : Option[DataSynthConfig]= None
  private[spark] var sparkSession : Option[SparkSession] = None

  def start(config:DataSynthConfig):Unit={
    this.config = Some(config)
    this.sparkSession = Some(SparkSession.builder().getOrCreate())
  }


  private[spark] def getSparkSession():SparkSession = {
    this.sparkSession match {
      case Some(s) => s
      case None => throw  new RuntimeException("SparkRuntime must be started.")
    }
  }

  private[spark] def getConfig(): DataSynthConfig = {
    this.config match {
      case Some(c) => c
      case None => throw  new RuntimeException("SparkRuntime must be started.")
    }
  }

  def run(executionPlan : Seq[ExecutionPlan.Table] ) = {

    val config:DataSynthConfig = getConfig()
    val sparkSession = getSparkSession()

    if(sparkSession.sparkContext.master == "yarn" &&
       !FileUtils.isHDFS(config.outputDir) ) {
      throw new RuntimeException(s"Wrong Datasynth output directory: " +
                                   s"${config.outputDir}. Outpud directory must be prefixed with " +
                                   s"hdfs:// when executing on yarn")

    }

    // Generate temporal jar with runtime generators
    val generatorBuilder = new RuntimePropertyGeneratorBuilder(config)
    val jarFileName:String = config.masterWorkspaceDir+"/temp.jar"

    val runtimeClasses : RuntimeClasses = generatorBuilder.codePropertyTableClasses(executionPlan)
    val runtimeCode:Map[String,String] = runtimeClasses.classNameToClassCode

    generatorBuilder.buildJar(FileUtils.removePrefix(jarFileName),runtimeCode)


    // Add jar to classpath
    val urlCl = new URLClassLoader( Array[URL](new URL(jarFileName)), getClass.getClassLoader());
    val fs:FileSystem = FileSystem.get(sparkSession.sparkContext.hadoopConfiguration)
    if(sparkSession.sparkContext.master == "yarn") {
      if(!FileUtils.isHDFS(config.datasynthWorkspaceDir)) {
        throw new RuntimeException(s"Wrong Datasynth workspace directory: " +
                                   s"${config.datasynthWorkspaceDir}. DataSynth's workspace directory" +
                                     s"must be prefixed with hdfs:// when executing on yarn")

      }

      fs.copyFromLocalFile(false,
                           true,
                           new Path(jarFileName),
                           new Path(config.datasynthWorkspaceDir+"/temp.jar"))
      sparkSession.sparkContext.addJar(config.datasynthWorkspaceDir+"/temp.jar")
    } else {
      sparkSession.sparkContext.addJar(jarFileName)
    }

    // Patch execution plan to replace old generators with new existing ones
    val classesNames:mutable.Map[String,String] = runtimeClasses.propertyTableNameToClassName
    val injectRuntimeGenerators = new InjectRuntimeGenerators(classesNames.toMap)
    val modifiedExecutionPlan:Seq[ExecutionPlan.Table] = injectRuntimeGenerators.run(executionPlan)

    // Execute execution plan
    val hdfsMaster = sparkSession.sparkContext.hadoopConfiguration.get("fs.default.name")
    val prefix = config.outputDir match {
      case path : String if FileUtils.isHDFS(path) => hdfsMaster +"/"+FileUtils.removePrefix(path)
      case path : String if FileUtils.isLocal(path) => FileUtils.removePrefix(path)
    }
    modifiedExecutionPlan.foreach(table =>
      fetchTableOperator(table).write.csv(prefix+"/"+table.name)
    )
  }

  def stop(): Unit = {
    fetchTableOperator.clear()
    fetchRndGeneratorOperator.clear()

    sparkSession match {
      case Some(s) => s.stop()
      case None => throw  new RuntimeException("SparkRuntime must be started.")
    }
  }
}





