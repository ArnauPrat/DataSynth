package org.dama.datasynth.runtime.operators

import org.apache.spark.sql.{Dataset, Encoder, SparkSession}
import org.dama.datasynth.common.PropertyGenerator
import org.dama.datasynth.executionplan.ExecutionPlan.PropertyTable
import org.dama.datasynth.runtime.SparkRuntime
import org.dama.datasynth.runtime.utils.RndGenerator

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
    val generator: PropertyGenerator[Boolean] = InstantiatePropertyGeneratorOperator[Boolean](node.generator)
    val size : Long = EvalValueOperator(node.size).asInstanceOf[Long]
    val rnd = FetchRndGenerator.execute(node.name);
    SparkRuntime.spark.range (0, size).map (i => Tuple2[Long,Boolean](i.toLong, generator.run (i, rnd.random (i) ) ) )
  }

  /**
    * Creates an Spark Dataset representing an int property table
    * @param node The execution plan node representing this operation
    * @return The created Dataset
    */
  def int(node : PropertyTable[Int] ) : Dataset[(Long,Int)] = {
    val generator: PropertyGenerator[Int] = InstantiatePropertyGeneratorOperator[Int](node.generator)
    val size : Long = EvalValueOperator(node.size).asInstanceOf[Long]
    val rnd = FetchRndGenerator.execute(node.name);
    SparkRuntime.spark.range (0, size).map (i => Tuple2[Long,Int](i.toLong, generator.run (i, rnd.random (i) ) ) )
  }

  /**
    * Creates an Spark Dataset representing a long property table
    * @param node The execution plan node representing this operation
    * @return The created Dataset
    */
  def long(node : PropertyTable[Long] ) : Dataset[(Long,Long)] = {
    val generator: PropertyGenerator[Long] = InstantiatePropertyGeneratorOperator[Long](node.generator)
    val size : Long = EvalValueOperator(node.size).asInstanceOf[Long]
    val rnd = FetchRndGenerator.execute(node.name);
    SparkRuntime.spark.range (0, size).map (i => Tuple2[Long,Long](i.toLong, generator.run (i, rnd.random (i) ) ) )
  }

  /**
    * Creates an Spark Dataset representing a float property table
    * @param node The execution plan node representing this operation
    * @return The created Dataset
    */
  def float(node : PropertyTable[Float] ) : Dataset[(Long,Float)] = {
    val generator: PropertyGenerator[Float] = InstantiatePropertyGeneratorOperator[Float](node.generator)
    val size : Long = EvalValueOperator(node.size).asInstanceOf[Long]
    val rnd = FetchRndGenerator.execute(node.name);
    SparkRuntime.spark.range (0, size).map (i => Tuple2[Long,Float](i.toLong, generator.run (i, rnd.random (i) ) ) )
  }

  /**
    * Creates an Spark Dataset representing a double property table
    * @param node The execution plan node representing this operation
    * @return The created Dataset
    */
  def double(node : PropertyTable[Double] ) : Dataset[(Long,Double)] = {
    val generator: PropertyGenerator[Double] = InstantiatePropertyGeneratorOperator[Double](node.generator)
    val size : Long = EvalValueOperator(node.size).asInstanceOf[Long]
    val rnd = FetchRndGenerator.execute(node.name);
    SparkRuntime.spark.range (0, size).map (i => Tuple2[Long,Double](i.toLong, generator.run (i, rnd.random (i) ) ) )
  }

  /**
    * Creates an Spark Dataset representing a string property table
    * @param node The execution plan node representing this operation
    * @return The created Dataset
    */
  def string(node : PropertyTable[String] ) : Dataset[(Long,String)] = {
    val generator: PropertyGenerator[String] = InstantiatePropertyGeneratorOperator[String](node.generator)
    val size : Long = EvalValueOperator(node.size).asInstanceOf[Long]
    val rnd = FetchRndGenerator.execute(node.name);
    SparkRuntime.spark.range (0, size).map (i => Tuple2[Long,String](i.toLong, generator.run (i, rnd.random (i) ) ) )
  }
}
