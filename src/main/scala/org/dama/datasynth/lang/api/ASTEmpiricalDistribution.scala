package org.dama.datasynth.lang.api

import org.dama.datasynth.lang.api.Keywords.EmpiricalKW

import scala.collection.mutable

/**
  * AST node representing an empirical distribution
  */
class EmpiricalDistribution extends ASTGenerator {
  var path = ""
  var dependencies = List[ASTType]()
  def from( path : String ) : EmpiricalDistribution = {
    this.path = path;
    this
  }

  def dependsOn ( t : ASTType ) : EmpiricalDistribution = {
    dependencies = dependencies :+ t
    this
  }
}

trait acceptsEmpiricalDistribution extends ASTType {
  def is( keyword : EmpiricalKW ) : EmpiricalDistribution = {
    val dist = new EmpiricalDistribution
    this.distribution = dist
    dist
  }
}
