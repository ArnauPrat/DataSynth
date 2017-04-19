package org.dama.datasynth.runtime.spark.operators

import org.dama.datasynth.executionplan.ExecutionPlan.{StaticValue, TableSize, Value}

/**
  * Created by aprat on 9/04/17.
  */
object EvalValueOperator {

  /** Evaluates the value of the given Value and returns its result as Any
    *
    * @param node The execution plan node representing the Value to evaluate
    * @return The actual value of the Value as an Any object
    */
  def apply( node : Value[_]) : Any = {
    node match {
      case v : StaticValue[_] => v.value
      case v : TableSize => TableSizeOperator(v)
    }
  }
}
