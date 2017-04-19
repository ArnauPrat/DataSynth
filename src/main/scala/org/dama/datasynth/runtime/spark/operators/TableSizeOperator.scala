package org.dama.datasynth.runtime.spark.operators

import org.apache.spark.sql.Dataset
import org.dama.datasynth.executionplan.ExecutionPlan.TableSize
import org.dama.datasynth.runtime.spark.SparkRuntime

/**
  * Created by aprat on 9/04/17.
  */
object TableSizeOperator {

  /**
    * Operator that execute the TableSize operation
    * @param node The execution plan node representing the table size operation
    * @return The size of the table
    */
  def apply( node : TableSize ) : Long = {
    FetchTableOperator(node.table).count
  }

}
