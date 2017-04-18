package org.dama.datasynth.runtime

import org.dama.datasynth.executionplan.ExecutionPlan
import org.dama.datasynth.executionplan.ExecutionPlan.{PropertyTable, TableSize}
import org.dama.datasynth.runtime.operators.{FetchTableOperator, TableSizeOperator}
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner

/**
  * Created by aprat on 11/04/17.
  */
@RunWith(classOf[JUnitRunner])
class SparkRuntimeTest extends FlatSpec with Matchers {


  " A boolean table " should " should contain all true " in {
    val value = ExecutionPlan.StaticValue[Boolean](true)
    val generator = ExecutionPlan.PropertyGenerator[Boolean]("org.dama.datasynth.runtime.generators.dummyBooleanPropertyGenerator",Seq(value),Seq())
    val size = ExecutionPlan.StaticValue[Long](1000)
    val createPropertyTable = PropertyTable[Boolean]("boolean","property",generator,size)
    SparkRuntime.run(Seq(createPropertyTable))
    FetchTableOperator.booleanTables.get("boolean.property").get.collect.foreach(t => t._2 should be (value.value))
  }

  " A float table " should " should contain all 1.0 " in {
    val value = ExecutionPlan.StaticValue[Float](1.0f)
    val generator = ExecutionPlan.PropertyGenerator[Float]("org.dama.datasynth.runtime.generators.dummyFloatPropertyGenerator",Seq(value),Seq())
    val size = ExecutionPlan.StaticValue[Long](1000)
    val createPropertyTable = PropertyTable[Float]("float","property",generator,size)
    SparkRuntime.run(Seq(createPropertyTable))
    FetchTableOperator.floatTables.get("float.property").get.collect.foreach( t => t._2 should be (value.value))
  }

  " A double table " should " should contain all 1.0 " in {
    val value = ExecutionPlan.StaticValue[Double](1.0)
    val generator = ExecutionPlan.PropertyGenerator[Double]("org.dama.datasynth.runtime.generators.dummyDoublePropertyGenerator",Seq(value),Seq())
    val size = ExecutionPlan.StaticValue[Long](1000)
    val createPropertyTable = PropertyTable[Double]("double","property",generator,size)
    SparkRuntime.run(Seq(createPropertyTable))
    FetchTableOperator.doubleTables.get("double.property").get.collect.foreach( t => t._2 should be (value.value))
  }

  " A long table " should " should contain all 1s " in {
    val num = ExecutionPlan.StaticValue[Long](1)
    val generator = ExecutionPlan.PropertyGenerator[Long]("org.dama.datasynth.runtime.generators.dummyLongPropertyGenerator",Seq(num),Seq())
    val size = ExecutionPlan.StaticValue[Long](1000)
    val createPropertyTable = PropertyTable[Long]("long","property",generator,size)
    SparkRuntime.run(Seq(createPropertyTable))
    FetchTableOperator.longTables.get("long.property").get.collect.foreach( t => t._2 should be (num.value))
  }

  " An int table " should " should contain all 1s " in {
    val num = ExecutionPlan.StaticValue[Int](1)
    val generator = ExecutionPlan.PropertyGenerator[Int]("org.dama.datasynth.runtime.generators.dummyIntPropertyGenerator",Seq(num),Seq())
    val size = ExecutionPlan.StaticValue[Long](1000)
    val createPropertyTable = PropertyTable[Int]("int","property",generator,size)
    SparkRuntime.run(Seq(createPropertyTable))
    FetchTableOperator.intTables.get("int.property").get.collect.foreach( t => t._2 should be (num.value))
  }

}
