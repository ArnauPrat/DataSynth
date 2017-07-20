package org.dama.datasynth.runtime.spark

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.sql.SparkSession
import org.dama.datasynth.DataSynthConfig
import org.dama.datasynth.executionplan.ExecutionPlan
import org.dama.datasynth.executionplan.ExecutionPlan.PropertyTable
import org.dama.datasynth.runtime.spark.operators.FetchTableOperator
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers, Suite}

/**
  * Created by aprat on 11/04/17.
  */
@RunWith(classOf[JUnitRunner])
class SparkRuntimeTest extends FlatSpec with Matchers with BeforeAndAfterAll {

  val config = DataSynthConfig().setOutputDir("file:///tmp/datasynth")

  " A boolean table " should " contain all true " in {
    SparkSession.builder().master("local[*]").getOrCreate()
    val value = ExecutionPlan.StaticValue[Boolean](true)
    val generator = ExecutionPlan.PropertyGenerator[Boolean]("org.dama.datasynth.common.generators.property.dummy.DummyBooleanPropertyGenerator",Seq(value),Seq())
    val size = ExecutionPlan.StaticValue[Long](1000)
    val createPropertyTable = PropertyTable[Boolean]("boolean","property",generator,size)
    SparkRuntime.start(config)
    SparkRuntime.run(Seq(createPropertyTable))
    SparkRuntime.fetchTableOperator.booleanTables.get("boolean.property") match {
      case Some(table) => table.collect.foreach( { case (id,pvalue) => pvalue should be (value.value)})
      case None => throw new RuntimeException("Boolean Table does not exist")
    }
    SparkRuntime.stop()
  }

  " A float table " should " contain all 1.0 " in {
    SparkSession.builder().master("local[*]").getOrCreate()
    val value = ExecutionPlan.StaticValue[Float](1.0f)
    val generator = ExecutionPlan.PropertyGenerator[Float]("org.dama.datasynth.common.generators.property.dummy.DummyFloatPropertyGenerator",Seq(value),Seq())
    val size = ExecutionPlan.StaticValue[Long](1000)
    val createPropertyTable = PropertyTable[Float]("float","property",generator,size)
    SparkRuntime.start(config)
    SparkRuntime.run(Seq(createPropertyTable))
    SparkRuntime.fetchTableOperator.floatTables.get("float.property") match {
      case Some(table) => table.collect.foreach( { case (id,pvalue) => pvalue should be (value.value)})
      case None => throw new RuntimeException("Float Table does not exist")
    }
    SparkRuntime.stop()
  }

  " A double table " should " contain all 1.0 " in {
    SparkSession.builder().master("local[*]").getOrCreate()
    val value = ExecutionPlan.StaticValue[Double](1.0)
    val generator = ExecutionPlan.PropertyGenerator[Double]("org.dama.datasynth.common.generators.property.dummy.DummyDoublePropertyGenerator",Seq(value),Seq())
    val size = ExecutionPlan.StaticValue[Long](1000)
    val createPropertyTable = PropertyTable[Double]("double","property",generator,size)
    SparkRuntime.start(config)
    SparkRuntime.run(Seq(createPropertyTable))
    SparkRuntime.fetchTableOperator.doubleTables.get("double.property") match {
      case Some(table) => table.collect.foreach( {case (id,pvalue) => pvalue should be (value.value)})
      case None => throw new RuntimeException("Double Table does not exist")
    }
    SparkRuntime.stop()
  }

  " A long table " should " contain all 1s " in {
    SparkSession.builder().master("local[*]").getOrCreate()
    val num = ExecutionPlan.StaticValue[Long](1)
    val generator = ExecutionPlan.PropertyGenerator[Long]("org.dama.datasynth.common.generators.property.dummy.DummyLongPropertyGenerator",Seq(num),Seq())
    val size = ExecutionPlan.StaticValue[Long](1000)
    val createPropertyTable = PropertyTable[Long]("long","property",generator,size)
    SparkRuntime.start(config)
    SparkRuntime.run(Seq(createPropertyTable))
    SparkRuntime.fetchTableOperator.longTables.get("long.property") match {
      case Some(table) => table.collect.foreach( { case (id,pvalue) => pvalue should be (num.value)})
      case None => throw new RuntimeException("Long Table does not exist")
    }
    SparkRuntime.stop()
  }

  " An int table " should " contain all 1s " in {
    SparkSession.builder().master("local[*]").getOrCreate()
    val num = ExecutionPlan.StaticValue[Int](1)
    val generator = ExecutionPlan.PropertyGenerator[Int]("org.dama.datasynth.common.generators.property.dummy.DummyIntPropertyGenerator",Seq(num),Seq())
    val size = ExecutionPlan.StaticValue[Long](1000)
    val createPropertyTable = PropertyTable[Int]("int","property",generator,size)
    SparkRuntime.start(config)
    SparkRuntime.run(Seq(createPropertyTable))
    SparkRuntime.fetchTableOperator.intTables.get("int.property") match {
      case Some(table) => table.collect.foreach( {case (id,pvalue) => pvalue should be (num.value)})
      case None => throw new RuntimeException("Int Table does not exist")
    }
    SparkRuntime.stop()
  }

  " A property table created with a dummyLongMultPropertyGenerator" should "contain the result of multiplying two long tables" in {
    SparkSession.builder().master("local[*]").getOrCreate()
    val size = ExecutionPlan.StaticValue[Long](1000)
    val num1 = ExecutionPlan.StaticValue[Long](2)
    val generator1 = ExecutionPlan.PropertyGenerator[Long]("org.dama.datasynth.common.generators.property.dummy.DummyLongPropertyGenerator",Seq(num1),Seq())
    val propertyTable1 = ExecutionPlan.PropertyTable[Long]("long","property1", generator1, size)

    val num2 = ExecutionPlan.StaticValue[Long](3)
    val generator2 = ExecutionPlan.PropertyGenerator[Long]("org.dama.datasynth.common.generators.property.dummy.DummyLongPropertyGenerator",Seq(num2),Seq())
    val propertyTable2 = ExecutionPlan.PropertyTable[Long]("long","property2", generator2, size)

    val generator3 = ExecutionPlan.PropertyGenerator[Long]("org.dama.datasynth.common.generators.property.dummy.DummyLongMultPropertyGenerator",Seq(),Seq(propertyTable1, propertyTable2))
    val propertyTable3 = ExecutionPlan.PropertyTable[Long]("long","property3", generator3, size)
    SparkRuntime.start(config)
    SparkRuntime.run(Seq(propertyTable1,propertyTable2,propertyTable3))
    SparkRuntime.fetchTableOperator.longTables.get("long.property3") match {
      case Some(table) => table.collect.foreach( { case (id,value) => value should be (6)})
      case None => throw new RuntimeException("Long Table does not exist")
    }
    SparkRuntime.stop()
  }

  override def afterAll(): Unit = {
    val fileSystem = FileSystem.get(new Configuration())
    fileSystem.delete( new Path(config.outputDir), true)
  }
}
