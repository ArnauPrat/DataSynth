package org.dama.datasynth.executionplan

import org.dama.datasynth.executionplan.ExecutionPlan._

/**
  * Created by aprat on 4/04/17.
  *
  * Visitor abstract class for Execution Plans, used to implement printers, semantic analyses or optimization passes
  */


abstract class ExecutionPlanVoidVisitor {

  protected val msg:String="The usage of this type of visitor is no supported for the current ExecutionPlanNode.";

  def visit(node: PropertyTable[_]) //Table
  def visit(node: EdgeTable) //Table
  def visit(node: Match) //Table
  def visit( node : PropertyGenerator[_] ) //Generator
  def visit( node : StructureGenerator ) //Generator
  def visit( node : StaticValue[_] ) //Value
  def visit( node : File) // File
  def visit( node : TableSize) //Table
}


abstract class TableVoidVisitor extends ExecutionPlanVoidVisitor{
  def visit( node : PropertyGenerator[_] ) =  throw new RuntimeException(msg);//Generator
  def visit( node : StructureGenerator )  = throw new RuntimeException(msg);//Generator
  def visit( node : StaticValue[_] )  = throw new RuntimeException(msg);//Value
  def visit( node : TableSize)  = throw new RuntimeException(msg);//Table
  def visit( node : File)  = throw new RuntimeException(msg);//Table
}

abstract class PropertyGeneratorVoidVisitor extends ExecutionPlanVoidVisitor {

  def visit(node: PropertyTable[_])  = throw new RuntimeException(msg);//Table
  def visit(node: EdgeTable)  = throw new RuntimeException(msg);//Table
  def visit(node: Match)  = throw new RuntimeException(msg);//Table
  def visit( node : StructureGenerator )  = throw new RuntimeException(msg);//Generator
  def visit( node : StaticValue[_] )  = throw new RuntimeException(msg);//Value
  def visit( node : TableSize)  = throw new RuntimeException(msg);//Table
  def visit( node : File)  = throw new RuntimeException(msg);//Table
}

abstract class GraphGeneratorVoidVisitor  extends ExecutionPlanVoidVisitor {

  def visit(node: PropertyTable[_]) = throw new RuntimeException(msg); //Table
  def visit(node: EdgeTable) = throw new RuntimeException(msg); //Table
  def visit(node: Match)  = throw new RuntimeException(msg);//Table
  def visit( node : PropertyGenerator[_] )  = throw new RuntimeException(msg);//Generator
  def visit( node : StaticValue[_] )  = throw new RuntimeException(msg);//Value
  def visit( node : TableSize)  = throw new RuntimeException(msg);//Table
  def visit( node : File)  = throw new RuntimeException(msg);//Table
}

abstract class ValueVoidVisitor  extends ExecutionPlanVoidVisitor {
  def visit(node: PropertyTable[_])  = throw new RuntimeException(msg);//Table
  def visit(node: EdgeTable)  = throw new RuntimeException(msg);//Table
  def visit(node: Match)  = throw new RuntimeException(msg);//Table
  def visit( node : PropertyGenerator[_] )  = throw new RuntimeException(msg);//Generator
  def visit( node : StructureGenerator )  = throw new RuntimeException(msg);//Generator
  def visit( node : TableSize)  = throw new RuntimeException(msg);//Table
}
