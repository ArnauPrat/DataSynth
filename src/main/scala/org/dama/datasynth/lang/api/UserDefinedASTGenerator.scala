package org.dama.datasynth.lang.api

class ASTUserDefinedGenerator() extends ASTGenerator {
  def using( path : String ) : ASTGenerator = this
  def dependsOn( t : ASTType ) : ASTGenerator = this
}
