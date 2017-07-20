package org.dama.datasynth.executionplan

import org.dama.datasynth.executionplan.ExecutionPlan._

/**
  * Created by aprat on 4/04/17.
  *
  * Returns an execution plan in its string form
  */
class ExecutionPlanPrinter extends ExecutionPlanVoidVisitor {

  var numIndents : Int = 0
  var indentString : String = "    "
  var stringBuilder : StringBuilder = new StringBuilder();

  def printExecutionPlan( node : ExecutionPlanNode ): String = {
    stringBuilder.clear
    node.accept(this);
    stringBuilder.mkString
  }

  private def spaces( numIndents : Int ) : String = {
    numIndents match {
      case 1 => ""
      case _ => indentString ++: spaces(numIndents-1)
    }
  }

  private def printstring( str : String ) = {
    stringBuilder.append(spaces(numIndents)+str+"\n")
  }

  override def visit(node: StaticValue[_]) = {
    numIndents+=1
    printstring(node.toString)
    numIndents-=1
  }

  override def visit(node: PropertyGenerator[_]) = {
    numIndents+=1
    printstring(node.toString)
    node.initParameters.foreach( p => p.accept(this))
    node.dependentPropertyTables.foreach( g => g.accept(this))
    numIndents-=1
  }

  override def visit(node: StructureGenerator) = {
    numIndents+=1
    printstring(node.toString)
    numIndents-=1
  }

  override def visit(node: PropertyTable[_]) = {
    numIndents+=1
    printstring(node.toString)
    node.size.accept(this)
    node.generator.accept(this)
    numIndents-=1
  }

  override def visit(node: EdgeTable) = {
    numIndents+=1
    printstring(node.toString)
    node.size.accept(this)
    node.structure.accept(this)
    numIndents-=1
  }

  override def visit(node: TableSize) = {
    numIndents+=1
    printstring(node.toString)
    node.table.accept(this)
    numIndents-=1
  }

  override def visit(node: Match) = {
    numIndents+=1
    printstring(node.toString)
    node.sourcePropertyTable.accept(this)
    node.edgeTable.accept(this)
    numIndents-=1
  }

  override def visit(node: File): Unit = {
    numIndents+=1
    printstring(node.toString)
    numIndents-=1
  }
}
