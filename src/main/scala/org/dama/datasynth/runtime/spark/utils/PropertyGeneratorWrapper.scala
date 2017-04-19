package org.dama.datasynth.runtime.spark.utils

import org.dama.datasynth.common.PropertyGenerator

/**
  * Created by aprat on 19/04/17.
  *
  * This class wraps a PropertyGenerator and a random number generator. The class provides
  * a run method to generate a property given a node id.
  */
case class PropertyGeneratorWrapper[T]( pg : PropertyGenerator[T],
                                        rndGen : RndGenerator,
                                        dependentPGs : Seq[PropertyGeneratorWrapper[_]]) {
  def run( id: Long ): T = {
    val params : Seq[Any] = dependentPGs.map( pg => pg.run(id))
    pg.run(id,rndGen.random(id),params : _*)
  }

}
