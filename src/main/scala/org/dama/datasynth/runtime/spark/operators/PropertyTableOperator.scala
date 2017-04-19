package org.dama.datasynth.runtime.spark.operators

import org.apache.spark.sql.{Dataset, Encoder, SparkSession}
import org.dama.datasynth.common.PropertyGenerator
import org.dama.datasynth.executionplan.ExecutionPlan.PropertyTable
import org.dama.datasynth.runtime.spark.SparkRuntime
import org.dama.datasynth.runtime.spark.utils.{PropertyGeneratorWrapper, RndGenerator}

/**
  * Created by aprat on 9/04/17.
  */
object PropertyTableOperator {

  import SparkRuntime.spark.implicits._

  /**
    * Creates an Spark Dataset representing a boolean property table
    * @param node The execution plan node representing this operation
    * @return The created Dataset
    */
  def boolean(node : PropertyTable[Boolean] ) : Dataset[(Long,Boolean)] = {
    val generator: PropertyGeneratorWrapper[Boolean] = InstantiatePropertyGeneratorOperator[Boolean](node.name, node.generator)
    val size : Long = EvalValueOperator(node.size).asInstanceOf[Long]
    val rnd = FetchRndGenerator.execute(node.name);
    SparkRuntime.spark.range (0, size).map (i => Tuple2[Long,Boolean](i.toLong, generator.run (i) ) )
  }

  /**
    * Creates an Spark Dataset representing an int property table
    * @param node The execution plan node representing this operation
    * @return The created Dataset
    */
  def int(node : PropertyTable[Int] ) : Dataset[(Long,Int)] = {
    val generator: PropertyGeneratorWrapper[Int] = InstantiatePropertyGeneratorOperator[Int](node.name, node.generator)
    val size : Long = EvalValueOperator(node.size).asInstanceOf[Long]
    SparkRuntime.spark.range (0, size).map (i => Tuple2[Long,Int](i.toLong, generator.run (i) ) )
  }

  /**
    * Creates an Spark Dataset representing a long property table
    * @param node The execution plan node representing this operation
    * @return The created Dataset
    */
  def long(node : PropertyTable[Long] ) : Dataset[(Long,Long)] = {
    val generator: PropertyGeneratorWrapper[Long] = InstantiatePropertyGeneratorOperator[Long](node.name, node.generator)
    val size : Long = EvalValueOperator(node.size).asInstanceOf[Long]
    SparkRuntime.spark.range (0, size).map (i => Tuple2[Long,Long](i.toLong, generator.run (i) ) )
  }

  /**
    * Creates an Spark Dataset representing a float property table
    * @param node The execution plan node representing this operation
    * @return The created Dataset
    */
  def float(node : PropertyTable[Float] ) : Dataset[(Long,Float)] = {
    val generator: PropertyGeneratorWrapper[Float] = InstantiatePropertyGeneratorOperator[Float](node.name, node.generator)
    val size : Long = EvalValueOperator(node.size).asInstanceOf[Long]
    SparkRuntime.spark.range (0, size).map (i => Tuple2[Long,Float](i.toLong, generator.run (i) ) )
  }

  /**
    * Creates an Spark Dataset representing a double property table
    * @param node The execution plan node representing this operation
    * @return The created Dataset
    */
  def double(node : PropertyTable[Double] ) : Dataset[(Long,Double)] = {
    val generator: PropertyGeneratorWrapper[Double] = InstantiatePropertyGeneratorOperator[Double](node.name, node.generator)
    val size : Long = EvalValueOperator(node.size).asInstanceOf[Long]
    SparkRuntime.spark.range (0, size).map (i => Tuple2[Long,Double](i.toLong, generator.run (i) ) )
  }

  /**
    * Creates an Spark Dataset representing a string property table
    * @param node The execution plan node representing this operation
    * @return The created Dataset
    */
  def string(node : PropertyTable[String] ) : Dataset[(Long,String)] = {
    val generator: PropertyGeneratorWrapper[String] = InstantiatePropertyGeneratorOperator[String](node.name, node.generator)
    val size : Long = EvalValueOperator(node.size).asInstanceOf[Long]
    SparkRuntime.spark.range (0, size).map (i => Tuple2[Long,String](i.toLong, generator.run(i) ) )
  }
}
