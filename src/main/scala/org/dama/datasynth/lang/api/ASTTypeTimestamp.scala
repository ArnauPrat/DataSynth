package org.dama.datasynth.lang.api

import org.dama.datasynth.lang.api.Keywords.UniformKW

/**
  * Trait that implements the methods to allow parsing dates from strings to long
  */
trait StringDefinable extends ASTUniformDistribution[Long] {
  def min( s : String ) : StringDefinable = {
    // code to parse string date to long here
    this
  }
  def max( s : String ) : StringDefinable = {
    // code to parse string date to long here
    this
  }
}

class ASTTypeTimestamp extends ASTTypeNumeric[Long] {

  /**
    * Class used to apply a dirty trick, because scala does not allow to extend an exhisting instance with a trait
    * @param dist
    */
  class ASTTimestampUniformDistribution(var dist : ASTUniformDistribution[Long]) extends ASTUniformDistribution[Long] with StringDefinable {
    override def min(): Long = dist.min

    override def max(): Long = dist.max

    override def min(value: Long): ASTUniformDistribution[Long] = dist.min(value)

    override def max(value: Long): ASTUniformDistribution[Long] = dist.max(value)
  }


  /**
    * Method that overrides the is method from acceptsUniformDistribution trait
    * @param uniform
    * @param tc
    * @return
    */
  override def is ( uniform : UniformKW)(implicit tc: ASTUniformDistributionProvider[Long]) :
  ASTUniformDistribution[Long] with StringDefinable = {
    new ASTTimestampUniformDistribution(super.is(uniform))
  }
}


