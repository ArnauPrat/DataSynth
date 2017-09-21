package org.dama.datasynth.lang

import org.dama.datasynth.lang.api._


object api {

  trait UniformKW
  object uniform extends UniformKW

  trait GeneratedKW
  object generated extends GeneratedKW

  trait EmpiricalKW
  object empirical extends EmpiricalKW

  type TypeInt = ASTTypeNumeric[Int]
  type TypeLong = ASTTypeNumeric[Long]

  type TypeString = ASTTypeString
  type TypeTimestamp = ASTTypeTimestamp


}
