package org.dama.datasynth.common.generators.property.empirical

import org.dama.datasynth.common.utils.FileUtils
import org.dama.datasynth.runtime.spark.utils.RndGenerator
import org.junit.runner.RunWith
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner

/**
  * Created by aprat on 12/05/17.
  */
@RunWith(classOf[JUnitRunner])
class DistributionGeneratorTests extends FlatSpec with Matchers with BeforeAndAfterAll {

  "An Int generator using /distributions/intDistribution.txt" should "return randomly uniform values between 0 and 9" in {
    val random = new RndGenerator(0)
    val intGenerator = new IntGenerator(FileUtils.File("file://./src/main/resources/distributions/intDistribution.txt")," ")
    val generatedNumbers = Range(0,1000000).map( i => intGenerator.run(i,random.random(i)))
    val densities : List[(Int,Double)] = generatedNumbers.groupBy({ case number => number }).map( { case (number,list) => number -> list.size/1000000.toDouble}).toList
    densities.foreach( {case (number,density) => assert(Math.abs(density-0.1) < 0.005) })
  }

}
