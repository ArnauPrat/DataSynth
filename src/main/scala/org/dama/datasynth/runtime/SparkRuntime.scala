package org.dama.datasynth.runtime

import org.apache.spark.sql.{Dataset, SparkSession}
import org.dama.datasynth.runtime.operators.{FetchTableOperator, PropertyTableOperator}
import org.dama.datasynth.executionplan.{ExecutionPlan, ExecutionPlanVoidVisitor}

import scala.collection.mutable

/**
  * Created by aprat on 6/04/17.
  */
object SparkRuntime {

  val spark = SparkSession
    .builder()
    .appName("Spark SQL basic example")
    .master("local")
    .config("spark.some.config.option", "some-value")
    .getOrCreate()



  // Map used to store the edge tables
  var edgeTables = new mutable.HashMap[String,Dataset[(Long,Long)]]

  def run( executionPlan : Seq[ExecutionPlan.Table] ) = {
    executionPlan.foreach(x => FetchTableOperator.apply(x).collect())
  }
}
