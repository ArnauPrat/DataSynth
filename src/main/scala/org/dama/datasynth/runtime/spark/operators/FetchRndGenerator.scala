package org.dama.datasynth.runtime.spark.operators

import org.dama.datasynth.runtime.spark.SparkRuntime
import org.dama.datasynth.runtime.spark.utils.RndGenerator

import scala.collection.mutable

/**
  * Created by aprat on 18/04/17.
  */
object FetchRndGenerator {

  // Map used to store the random number generators used by each property table
  var rndGenerators = new mutable.HashMap[String, RndGenerator]

  // Next random number generator seed
  var nextRndGeneratorSeed = 0L;

  def execute( tableName : String ) : RndGenerator = {
    rndGenerators.get(tableName) match {
      case Some(r) => r
      case None => {
        val rndGenerator = new RndGenerator(nextRndGeneratorSeed)
        nextRndGeneratorSeed+=1
        rndGenerators.put(tableName,rndGenerator)
        rndGenerator
      }
    }
  }

}
