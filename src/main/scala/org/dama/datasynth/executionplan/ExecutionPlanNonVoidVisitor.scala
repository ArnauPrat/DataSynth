package org.dama.datasynth.executionplan

import org.dama.datasynth.executionplan.ExecutionPlan._

/**
  * Created by aprat on 4/04/17.
  *
  * Visitor abstract class for Execution Plans, used to implement printers, semantic analyses or optimization passes
  */
abstract class ExecutionPlanNonVoidVisitor[T] {

  protected val msg:String="The usage of this type of visitor is no supported for the current ExecutionPlanNode.";

  def visit( node : StaticValue[_] ) : T
  def visit( node : PropertyGenerator[_] ) : T
  def visit( node : StructureGenerator ) : T
  def visit( node : PropertyTable[_] ) : T
  def visit( node : EdgeTable ) : T
  def visit( node : TableSize ) : T
  def visit( node : Match ) : T
  def visit( node : File ) : T
}

abstract class TableNonVoidVisitor[T] extends ExecutionPlanNonVoidVisitor[T]{
  def visit( node : PropertyGenerator[_] ) = throw new RuntimeException(msg);//Generator
  def visit( node : StructureGenerator )  = throw new RuntimeException(msg);//Generator
  def visit( node : StaticValue[_] )  = throw new RuntimeException(msg);//Value
  def visit( node : TableSize)  = throw new RuntimeException(msg);//Table
  def visit( node : File)  = throw new RuntimeException(msg);//Table
}

abstract class PropertyGeneratorNonVoidVisitor extends ExecutionPlanNonVoidVisitor {

  def visit(node: PropertyTable[_])  = throw new RuntimeException(msg);//Table
  def visit(node: EdgeTable)  = throw new RuntimeException(msg);//Table
  def visit(node: Match)  = throw new RuntimeException(msg);//Table
  def visit( node : StructureGenerator )  = throw new RuntimeException(msg);//Generator
  def visit( node : StaticValue[_] )  = throw new RuntimeException(msg);//Value
  def visit( node : TableSize)  = throw new RuntimeException(msg);//Table
  def visit( node : File)  = throw new RuntimeException(msg);//Table
}

abstract class GraphGeneratorNonVoidVisitor  extends ExecutionPlanNonVoidVisitor {
  def visit(node: PropertyTable[_]) = throw new RuntimeException(msg); //Table
  def visit(node: EdgeTable) = throw new RuntimeException(msg); //Table
  def visit(node: Match)  = throw new RuntimeException(msg);//Table
  def visit( node : PropertyGenerator[_] )  = throw new RuntimeException(msg);//Generator
  def visit( node : StaticValue[_] )  = throw new RuntimeException(msg);//Value
  def visit( node : TableSize)  = throw new RuntimeException(msg);//Table
  def visit( node : File)  = throw new RuntimeException(msg);//Table
}

abstract class ValueNonVoidVisitor  extends ExecutionPlanNonVoidVisitor {
  def visit(node: PropertyTable[_])  = throw new RuntimeException(msg);//Table
  def visit(node: EdgeTable)  = throw new RuntimeException(msg);//Table
  def visit(node: Match)  = throw new RuntimeException(msg);//Table
  def visit( node : PropertyGenerator[_] )  = throw new RuntimeException(msg);//Generator
  def visit( node : StructureGenerator )  = throw new RuntimeException(msg);//Generator
  def visit( node : TableSize)  = throw new RuntimeException(msg);//Table
}
