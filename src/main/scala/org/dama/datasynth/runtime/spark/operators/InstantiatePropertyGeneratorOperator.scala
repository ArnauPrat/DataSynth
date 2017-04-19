package org.dama.datasynth.runtime.spark.operators

import org.dama.datasynth.executionplan.ExecutionPlan.PropertyGenerator
import org.dama.datasynth.runtime.spark.utils._
import org.dama.datasynth._

/**
  * Created by aprat on 9/04/17.
  */
object InstantiatePropertyGeneratorOperator {

  /**
    * Instantiates a property generator
    * @param info The execution plan node representing the property generator
    * @tparam T The type of the property generated by the instantiated property generator
    * @return The instantiated property generator
    */
  def apply[T](propertyTableName : String, info : PropertyGenerator[T]) : PropertyGeneratorWrapper[T] = {
    var generator = common.PropertyGenerator.getInstance[T](info.className)
    val initParameters : Seq[Any] = info.initParameters.map( x => EvalValueOperator(x))
    generator.initialize(initParameters : _*)
    val rndGen = FetchRndGenerator.execute(propertyTableName)
    val dependentPGs = info.dependentPropertyTables.map( pt => InstantiatePropertyGeneratorOperator(pt.name, pt.generator))
    PropertyGeneratorWrapper[T](generator,rndGen,dependentPGs)
  }
}
