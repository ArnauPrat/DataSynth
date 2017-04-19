package org.dama.datasynth.runtime.spark.operators

import org.dama.datasynth.executionplan.ExecutionPlan
import org.dama.datasynth.executionplan.ExecutionPlan.{PropertyTable, StaticValue, TableSize}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by aprat on 12/04/17.
  */
@RunWith(classOf[JUnitRunner])
class OperatorsTest extends FlatSpec with Matchers {

  " An TableSizeOperator on a table of size 1000" should " should return 1000 " in {
    val num = ExecutionPlan.StaticValue[Int](1)
    val generator = ExecutionPlan.PropertyGenerator[Int]("org.dama.datasynth.runtime.generators.dummyIntPropertyGenerator",Seq(num),Seq())
    val size = ExecutionPlan.StaticValue[Long](1000)
    val createPropertyTable = PropertyTable[Int]("int","property",generator,size)
    val tableSize = TableSize(createPropertyTable)
    TableSizeOperator(tableSize) should be (1000)
  }

  " An EvalValueOperator on a StaticValue[Boolean](true)" should " should return a Boolean true" in {
    val value = StaticValue[Boolean](true)
    EvalValueOperator(value).asInstanceOf[Boolean] should be (true)
  }

  " An EvalValueOperator on a StaticValue[Int](1)" should " should return an Int 1" in {
    val value = StaticValue[Int](1)
    EvalValueOperator(value).asInstanceOf[Int] should be (1)
  }

  " An EvalValueOperator on a StaticValue[Long](1)" should " should return a Long 1" in {
    val value = StaticValue[Long](1)
    EvalValueOperator(value).asInstanceOf[Long] should be (1)
  }

  " An EvalValueOperator on a StaticValue[Float](1.0)" should " should return a Float 1.0" in {
    val value = StaticValue[Float](1)
    EvalValueOperator(value).asInstanceOf[Float] should be (1.0)
  }

  " An EvalValueOperator on a StaticValue[Double](1.0)" should " should return a Double 1.0" in {
    val value = StaticValue[Double](1)
    EvalValueOperator(value).asInstanceOf[Double] should be (1.0)
  }

  " An EvalValueOperator on a StaticValue[String](\"text\")" should " should return a String \"text\"0" in {
    val value = StaticValue[String]("text")
    EvalValueOperator(value).asInstanceOf[String] should be ("text")
  }

}
