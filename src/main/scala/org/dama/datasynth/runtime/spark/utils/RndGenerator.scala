package org.dama.datasynth.runtime.spark.utils

import scala.util.hashing.MurmurHash3

/**
  * Created by aprat on 6/04/17.
  *
  * Random number generator with skip seed
  * This is a temporal implementation that uses a murmurhash. The goal is to provide
  * uniformly distributed values
  */
case class RndGenerator( seed : Long ) {

  /**
    * Generates the ith random number in a sequence
    *
    * @param i The ith random number we are interested in
    * @return The ith random number in the sequence
    */
  def random( i : Long ) : Long =  MurmurHash3.stringHash( (i+seed).toString)
}
