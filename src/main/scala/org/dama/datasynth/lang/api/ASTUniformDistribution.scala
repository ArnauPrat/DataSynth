package org.dama.datasynth.lang.api


/**
  * Abstract class representinga a uniform distribution description
  * @tparam T The underlying value type of the distribution
  */
trait ASTUniformDistribution[T] extends ASTGenerator {
  def min() : T
  def max() : T
  def min( value : T) : ASTUniformDistribution[T]
  def max( value : T) : ASTUniformDistribution[T]
}


/**
  * Type Class trait, used to provide instances of UniformDistributions for specific types
  * @tparam T The underlying type of the distribution to provide
  */
sealed trait ASTUniformDistributionProvider[T] {
  def create : ASTUniformDistribution[T]
}

/**
  * Type Class companion object
  */
object ASTUniformDistributionProvider {

  // Int Uniform Distribution provider
  implicit val intUniformDistributionProvider  = new  ASTUniformDistributionProvider[Int] {
    override def create : ASTUniformDistribution[Int] = new ASTUniformDistribution[Int] {
      var min_ = 0
      var max_ = 0

      override def min: Int = min_
      override def max: Int = max_

      override def min(value: Int) : ASTUniformDistribution[Int] = {
        min_ = value
        this
      }

      override def max(value: Int) : ASTUniformDistribution[Int] = {
        max_ = value
        this
      }
    }
  }

    // Long Uniform Distribution provider
  implicit val longUniformDistributionProvider  = new ASTUniformDistributionProvider[Long] {

    override def create : ASTUniformDistribution[Long] = new ASTUniformDistribution[Long] {

      var min_ = 0L
      var max_ = 0L

      override def min: Long = min_
      override def max: Long = max_

      override def min(value: Long) : ASTUniformDistribution[Long] = {
        min_ = value
        this
      }

      override def max(value: Long) : ASTUniformDistribution[Long] = {
        max_ = value
        this
      }
    }
  }

    // Float Uniform Distribution provider
    implicit val floatUniformDistributionProvider = new ASTUniformDistributionProvider[Float] {
      def create : ASTUniformDistribution[Float] = new  ASTUniformDistribution[Float] {

        var min_ = 0.0f
        var max_ = 0.0f

        override def min: Float = min_
        override def max: Float = max_

        override def min(value: Float) : ASTUniformDistribution[Float] = {
          min_ = value
          this
        }

        override def max(value: Float) : ASTUniformDistribution[Float] = {
          max_ = value
          this
        }
      }
    }

    // Double Uniform Distribution provider
    implicit val doubleUniformDistributionProvider  = new ASTUniformDistributionProvider[Double] {
      def create : ASTUniformDistribution[Double] = new ASTUniformDistribution[Double] {

        var min_ = 0.0
        var max_ = 0.0

        override def min: Double = min_
        override def max: Double = max_
        override def min(value: Double) : ASTUniformDistribution[Double] = {
          min_ = value
          this
        }
        override def max(value: Double) : ASTUniformDistribution[Double] = {
          max_ = value
          this
        }
      }
    }
}

import ASTUniformDistributionProvider._
import org.dama.datasynth.lang.api.Keywords.UniformKW

/**
  * Trait representing those types that accept uniform distributions (e.g. Int, Float, Double, etc.)
  * @tparam T The underlying type of the distribution that accepts
  */
trait acceptsUniformDistribution[T] extends ASTType {
  def is ( uniform : UniformKW)(implicit tc: ASTUniformDistributionProvider[T]) : ASTUniformDistribution[T] = {
    val dist : ASTUniformDistribution[T] = tc.create
    this.distribution = dist
    dist
  }
}


