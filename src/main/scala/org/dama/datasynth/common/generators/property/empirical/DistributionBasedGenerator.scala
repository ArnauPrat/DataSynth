package org.dama.datasynth.common.generators.property.empirical

import java.io.{BufferedReader, InputStreamReader}

import org.dama.datasynth.common.generators.property.PropertyGenerator
import org.dama.datasynth.common.utils.FileUtils.File
import org.dama.datasynth.runtime.spark.utils.RndGenerator

import scala.io.Source

/**
  * Created by aprat on 12/05/17.
  *
  * Property generator based on a distribution file. The file has two columns [value,probability],
  * where probability is the marginal probability of observing the given value
  * Probabilities of the second column must add 1 or be very close.
  */
class DistributionBasedGenerator[T](parser: (String) => T, file : File, separator: String)
  extends PropertyGenerator[T] {

  private val inputFileLines : List[String] = file.open().toList

  private val values : List[(T,Double)]  = inputFileLines.map( line => line.split(separator))
    .map( { case Array(value,prob) => (parser(value),prob.toDouble)})

  private val probabilitiesSum = values.foldLeft(0.0)( { case (acc, (_,prob)) => acc + prob} )

  if( (1.0-probabilitiesSum) > 0.001 || probabilitiesSum > 1.0) {
    throw new RuntimeException(s"Invalid input file. Probabilities do not add 1 but $probabilitiesSum")
  }

  val data = values.drop(1)
    .scanLeft(values(0))({ case ((prevValue,accProb),(value,prob)) => (value,accProb+prob)})
    .toArray

  /**
    * Performs a binary search over the given array containing pairs value-probability, where probability is the
    * cumulative probability of observing such value. Thus, the array must be sorted increasingly by the probability.
    * The method returns the value whose probability is closer to the given probability, which has been obtained
    * uniformly. This method is widely known as the inverse sampling method.
    * @param data The array with pairs of value-probability
    * @param prob
    * @return
    */
  def binarySearchForValue(data : Array[(T,Double)], prob : Double) : T = {
    def doSearch(data : Array[(T,Double)]) : T = {
      if (data.size == 1) {
        data(0) match { case (value,prob) => value }
      } else {
        val middleIndex:Int = data.size / 2
        val (left,right):(Array[(T,Double)],Array[(T,Double)]) = data.splitAt(middleIndex)
        (data(middleIndex-1), data(middleIndex)) match {
          case ((_,_),(value, valProb)) if valProb == prob => value
          case ((_,_),(value, valProb)) if valProb < prob => doSearch(right)
          case ((value, valProb),(_,_)) if valProb > prob => doSearch(left)
          case ((_, _),(value,_)) => value
        }
      }
    }
    doSearch(data)
  }

  override def run(id: Long, random: Long, dependencies: Any*) : T = {
    val prob:Double = RndGenerator.toDouble(random)
    binarySearchForValue(data,prob)
  }
}
