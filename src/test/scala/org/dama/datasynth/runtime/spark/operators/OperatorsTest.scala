package org.dama.datasynth.runtime.spark.operators

import org.apache.spark.sql.SparkSession
import org.dama.datasynth.DataSynthConfig
import org.dama.datasynth.executionplan.ExecutionPlan
import org.dama.datasynth.executionplan.ExecutionPlan.{EdgeTable, PropertyTable, StaticValue, TableSize}
import org.dama.datasynth.runtime.spark.SparkRuntime
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by aprat on 12/04/17.
  */
@RunWith(classOf[JUnitRunner])
class OperatorsTest extends FlatSpec with Matchers {

  val config = DataSynthConfig().setOutputDir("file:///tmp/datasynth")
                                .schemaFile("file:///tmp/fake.json")
  DataSynthConfig.validateConfig(config)

  " An TableSizeOperator on a table of size 1000" should " should return 1000 " in {
    SparkSession.builder().master("local[*]").getOrCreate()
    val num = ExecutionPlan.StaticValue[Int](1)
    val generator = ExecutionPlan.PropertyGenerator[Int]("org.dama.datasynth.common.generators.property.dummy.DummyIntPropertyGenerator",Seq(num),Seq())
    val size = ExecutionPlan.StaticValue[Long](1000)
    val createPropertyTable = PropertyTable[Int]("int","property",generator,size)
    val tableSize = TableSize(createPropertyTable)
    SparkRuntime.start(config)
    SparkRuntime.tableSizeOperator(tableSize) should be (1000)
    SparkRuntime.stop()
  }

  " An InstantiatePropertyGeneratorOperator" should "return an instance of a property generator " in {
    SparkSession.builder().master("local[*]").getOrCreate()
    val num = ExecutionPlan.StaticValue[Int](1)
    val propertyGeneratorNode = ExecutionPlan.PropertyGenerator[Int]("org.dama.datasynth.common.generators.property.dummy.DummyIntPropertyGenerator",Seq(num),Seq())
    SparkRuntime.start(config)
    val generator = SparkRuntime.instantiatePropertyGeneratorOperator("table.property", propertyGeneratorNode)
    println(generator(0))
    SparkRuntime.stop()
  }

  " An InstantiateGraphGeneratorOperator" should "return an instance of a property generator " in {
    SparkSession.builder().master("local[*]").getOrCreate()
    val file1 = ExecutionPlan.File("file:///path/to/file")
    val file2 = ExecutionPlan.File("file:///path/to/file")
    val structureGeneratorNode = ExecutionPlan.StructureGenerator("org.dama.datasynth.common.generators.structure.BTERGenerator",Seq(file1, file2))
    SparkRuntime.start(config)
    val generator = SparkRuntime.instantiateStructureGeneratorOperator(structureGeneratorNode)
    SparkRuntime.stop()
  }

  "A FetchTableOperator1" should "return a Dataset when fetching a table (either property or edge)" in {
    SparkSession.builder().master("local[*]").getOrCreate()
    val num = ExecutionPlan.StaticValue[Int](1)
    val propertyGenerator = ExecutionPlan.PropertyGenerator[Int]("org.dama.datasynth.common.generators.property.dummy.DummyIntPropertyGenerator",Seq(num),Seq())
    val size = ExecutionPlan.StaticValue[Long](1000)
    val createPropertyTable = PropertyTable[Int]("int","property",propertyGenerator,size)
    SparkRuntime.start(config)
    SparkRuntime.fetchTableOperator( createPropertyTable)
    SparkRuntime.stop()
  }
  "A FetchTableOperator2" should "return a Dataset when fetching a table (either property or edge)" in {
    SparkSession.builder().master("local[*]").getOrCreate()
    val currentDirectory = new java.io.File(".").getCanonicalPath
    val file1 = ExecutionPlan.File("file://"+currentDirectory+"/src/main/resources/degrees/dblp")
    val file2 = ExecutionPlan.File("file://"+currentDirectory+"/src/main/resources/ccs/dblp")
    val structureGenerator = ExecutionPlan.StructureGenerator("org.dama.datasynth.common.generators.structure.BTERGenerator",Seq(file1, file2))
    val size = ExecutionPlan.StaticValue[Long](1000)
    val createEdgeTable = EdgeTable("edges",structureGenerator,size)
    SparkRuntime.start(config)
    SparkRuntime.fetchTableOperator(createEdgeTable)
    SparkRuntime.stop()
  }

  "A FetchRndGeneratOperator generator" should "return a random generator which is unique for each table" in {
    SparkSession.builder().master("local[*]").getOrCreate()
    SparkRuntime.start(config)
    val rnd1 = SparkRuntime.fetchRndGeneratorOperator("table1")
    val rnd1Prima = SparkRuntime.fetchRndGeneratorOperator("table1")
    val rnd2 = SparkRuntime.fetchRndGeneratorOperator("table2")
    val rnd2Prima = SparkRuntime.fetchRndGeneratorOperator("table2")
    rnd1 should be (rnd1Prima)
    rnd2 should be (rnd2Prima)
    rnd1 should not be (rnd2)
    SparkRuntime.stop()
  }

  " An EvalValueOperator on a StaticValue[Boolean](true)" should " should return a Boolean true" in {
    SparkSession.builder().master("local[*]").getOrCreate()
    val value = StaticValue[Boolean](true)
    SparkRuntime.start(config)
    SparkRuntime.evalValueOperator(value).asInstanceOf[Boolean] should be (true)
    SparkRuntime.stop()
  }

  " An EvalValueOperator on a StaticValue[Int](1)" should " should return an Int 1" in {
    SparkSession.builder().master("local[*]").getOrCreate()
    val value = StaticValue[Int](1)
    SparkRuntime.start(config)
    SparkRuntime.evalValueOperator(value).asInstanceOf[Int] should be (1)
    SparkRuntime.stop()
  }

  " An EvalValueOperator on a StaticValue[Long](1)" should " should return a Long 1" in {
    SparkSession.builder().master("local[*]").getOrCreate()
    val value = StaticValue[Long](1)
    SparkRuntime.start(config)
    SparkRuntime.evalValueOperator( value).asInstanceOf[Long] should be (1)
    SparkRuntime.stop()
  }

  " An EvalValueOperator on a StaticValue[Float](1.0)" should " should return a Float 1.0" in {
    SparkSession.builder().master("local[*]").getOrCreate()
    val value = StaticValue[Float](1)
    SparkRuntime.start(config)
    SparkRuntime.evalValueOperator( value).asInstanceOf[Float] should be (1.0)
    SparkRuntime.stop()
  }

  " An EvalValueOperator on a StaticValue[Double](1.0)" should " should return a Double 1.0" in {
    SparkSession.builder().master("local[*]").getOrCreate()
    val value = StaticValue[Double](1)
    SparkRuntime.start(config)
    SparkRuntime.evalValueOperator(value).asInstanceOf[Double] should be (1.0)
    SparkRuntime.stop()
  }

  " An EvalValueOperator on a StaticValue[String](\"text\")" should " should return a String \"text\"0" in {
    SparkSession.builder().master("local[*]").getOrCreate()
    val value = StaticValue[String]("text")
    SparkRuntime.start(config)
    SparkRuntime.evalValueOperator(value).asInstanceOf[String] should be ("text")
    SparkRuntime.stop()
  }
}
