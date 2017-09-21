package org.dama.datasynth.lang.api

trait ASTTypeNumeric[T] extends ASTType
                       with acceptsUniformDistribution[T]
                       with acceptsEmpiricalDistribution {
}

object ASTTypeNumeric {
  type TypeInt = ASTTypeNumeric[Int]
}


