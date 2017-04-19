package org.dama.datasynth.executionplan


import scala.reflect.runtime.universe._

/**
  * Created by aprat&joangui on 31/03/17.
  * Represents an execution plan. Effectively, its like a tree of ExecutionPlanNodes.
  * It contains several node types such as Parameter, Task or Generator
  * Some nodes produce values. These are indicated by "Producer" traits.
  * For instance, a long parameter is a LongProducer, similar as the Task "TableSize".
  */
object ExecutionPlan {

  /******************************************************/
  /** Base class for all execution plan nodes          **/
  /******************************************************/

  sealed abstract class ExecutionPlanNode {
    def accept( visitor : ExecutionPlanVoidVisitor )
    def accept[T]( visitor : ExecutionPlanNonVoidVisitor[T] ) : T
  };

  /********************************************************************/
  /** Abstract classes used to represent the differen type sof nodes **/
  /********************************************************************/

  /** Represents a value **/
  abstract class Value[T] extends ExecutionPlanNode {
    type parameterType = T
  }

  /** Represents a value whose value is known at compile time */
  case class StaticValue[T]( value : T) extends Value[T] {
    override def toString() : String = s"[StaticValue[${value.getClass.getSimpleName}],${value}]"

    override def accept(visitor: ExecutionPlanVoidVisitor) = visitor.visit(this)

    override def accept[T](visitor: ExecutionPlanNonVoidVisitor[T]): T = visitor.visit(this)
  }

  /** Produces a Table **/
  abstract class Table( val name : String ) extends ExecutionPlanNode

  /** Produces a Property Table **/
  abstract class AbstractPropertyTable[T : TypeTag]( name : String ) extends Table(name) {
    val tag  = typeTag[T]
  }

  /** Produces an Edge Tables **/
  abstract class AbstractEdgeTable( name : String ) extends Table(name)

  /******************************************************/
  /** Generators                                       **/
  /******************************************************/

  /**
    * Case class representing a property generator
    * @param className The name of the generator
    * @param initParameters The sequence of init parameters of the generator
    * @param dependentPropertyTables The property tables this generator depends on
    */
  case class PropertyGenerator[T]( className : String,
                                initParameters : Seq[Value[_]],
                                dependentPropertyTables : Seq[PropertyTable[_]]) extends ExecutionPlanNode {

    override def toString: String = s"[PropertyGenerator,$className]"

    override def accept(visitor: ExecutionPlanVoidVisitor) = visitor.visit(this)

    override def accept[T](visitor: ExecutionPlanNonVoidVisitor[T]): T =  visitor.visit(this)
  }

  /**
    * Case class representing a generator
    * @param className The name of the generator
    * @param initParameters The sequence of init parameters of the generator
    */
  case class GraphGenerator( className : String, initParameters : Seq[Value[_]]) extends ExecutionPlanNode {

    override def toString: String = s"[GraphGenerator,$className]"

    override def accept(visitor: ExecutionPlanVoidVisitor) = visitor.visit(this)

    override def accept[T](visitor: ExecutionPlanNonVoidVisitor[T]): T = visitor.visit(this)
  }

  /******************************************************/
  /** Nodes producing tables                             **/
  /******************************************************/

  /**
    * Represents a create property table operation
    *
    * @param typeName The name of the type this property table belongs to
    * @param propertyName The name of the property of this property table
    * @param generator The property generator to create the table
    * @param size  The LongProducer to obtain the size of the table from
    */
  case class PropertyTable[T : TypeTag](typeName : String, propertyName : String, generator : PropertyGenerator[T], size : Value[Long] )
    extends AbstractPropertyTable[T](s"${typeName}.${propertyName}") {
    override def toString: String = s"[PropertyTable,$typeName.$propertyName]"

    override def accept( visitor : ExecutionPlanVoidVisitor ) = visitor.visit(this)

    override def accept[T](visitor: ExecutionPlanNonVoidVisitor[T]): T = visitor.visit(this)
  }

  /**
    * Represents a create edge table operation
    * @param generator The edge generator to create the table
    * @param size  The LongProducer to obtain the size of the table from
    */
  case class EdgeTable(tableName : String, generator : GraphGenerator, size : Value[Long] )
    extends AbstractEdgeTable(tableName) {
    override def toString: String = s"[EdgeTable,$tableName]"

    override def accept( visitor : ExecutionPlanVoidVisitor ) = visitor.visit(this)

    override def accept[T](visitor: ExecutionPlanNonVoidVisitor[T]): T = visitor.visit(this)

  }

  /**
    * Represents a size of table operation
    * @param table The table to compute the size from
    */
  case class TableSize( table : Table )
    extends Value[Long] {

    override def toString: String = "[TableSize]"

    override def accept( visitor : ExecutionPlanVoidVisitor ) = visitor.visit(this)

    override def accept[T](visitor: ExecutionPlanNonVoidVisitor[T]): T = visitor.visit(this)
  }

  /**
    * Represents a match operation between a property table and a graph
    *
    * @param tableName The name of the produced table
    * @param propertyTable The property table to match
    * @param graph The graph to match
    */
  case class Match(tableName : String, propertyTable : AbstractPropertyTable[_], graph : AbstractEdgeTable )
    extends AbstractEdgeTable(tableName) {
    override def toString: String = s"[Match,$tableName]"

    override def accept(visitor: ExecutionPlanVoidVisitor) =  visitor.visit(this)

    override def accept[T](visitor: ExecutionPlanNonVoidVisitor[T]): T = visitor.visit(this)
  }
}
