package org.dama.datasynth.runtime.spark

import org.apache.spark.sql.{Dataset, SparkSession}
import org.dama.datasynth.executionplan.ExecutionPlan
import org.dama.datasynth.runtime.spark.operators.FetchTableOperator

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

  def run( executionPlan : Seq[ExecutionPlan.Table] ) = {
    executionPlan.foreach(x => FetchTableOperator.apply(x).collect())
  }
}
