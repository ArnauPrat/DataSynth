package org.dama.datasynth.runtime.spark.operators

import org.dama.datasynth.common.utils.FileUtils
import org.dama.datasynth.executionplan.ExecutionPlan._
import org.dama.datasynth.runtime.spark.SparkRuntime

/**
  * Created by aprat on 9/04/17.
  *
  * Operator that evaluates the value of a Value execution plan node
  */
class EvalValueOperator {

  /** Evaluates the value of the given Value and returns its result as Any
    *
    * @param node The execution plan node representing the Value to evaluate
    * @return The actual value of the Value as an Any object
    */
  def apply(  node : Value[_]) : Any = {

    node match {
      case value : File => FileUtils.File(value.filename)
      case value : StaticValue[_] => value.value
      case value : TableSize => SparkRuntime.tableSizeOperator(value)
    }
  }
}
