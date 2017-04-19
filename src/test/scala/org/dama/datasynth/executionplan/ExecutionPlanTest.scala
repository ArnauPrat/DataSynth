package org.dama.datasynth.executionplan

import org.dama.datasynth.executionplan.ExecutionPlan._
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner

/**
  * Created by aprat on 31/03/17.
  */
@RunWith(classOf[JUnitRunner])
class ExecutionPlanTest extends FlatSpec with Matchers{

  /** Generator tests **/
  "A PropertyGenerator toString" should "output like [PropertyGenerator,propertyGeneratorName]" in {
    val generator = new PropertyGenerator[Long]("path.to.class", List[Value[_]](), List[PropertyTable[_]]() )
    generator.toString should be ("[PropertyGenerator,path.to.class]")
  }

  "A GraphGenerator toString" should "output like [GraphGenerator,graphGeneratorName]" in {
    val generator = GraphGenerator("path.to.class", Seq())
    generator.toString should be ("[GraphGenerator,path.to.class]")
  }

  /** Parameter tests **/
  "A StaticValue[Long] toString" should "output like [StaticValue,Long,value]" in {
    val parameter = StaticValue[Long](10)
    parameter.toString should be ("[StaticValue[Long],10]")
  }

  "A StaticValue[String] toString" should "output like [StaticValue,String,value]" in {
    val parameter = StaticValue[String]("test")
    parameter.toString should be ("[StaticValue[String],test]")
  }

  /** Tasks tests **/
  "A PropertyTable toString" should "output like [PropertyTable,typeName.PropertyName]" in {
    val generator = PropertyGenerator[Long]("path.to.generator",Seq(),Seq())
    val size = StaticValue[Long](10)
    val task = PropertyTable[Long]("typeName","propertyName", generator,size)
    task.toString should be ("[PropertyTable,typeName.propertyName]")
  }

  "A EdgeTable toString" should "output like [EdgeTable,tableName]" in {
    val generator = GraphGenerator("path.to.generator",Seq())
    val size = StaticValue[Long](10)
    val task = EdgeTable("tableName",generator,size)
    task.toString should be ("[EdgeTable,tableName]")
  }

  "A TableSize toString" should "output like [TableSize]" in {
    val generator = PropertyGenerator[Long]("path.to.generator",Seq(),Seq())
    val size = StaticValue[Long](10)
    val createTable = PropertyTable("typeName","propertyName", generator,size)
    val task = TableSize(createTable)
    task.toString should be ("[TableSize]")
  }

  "A Match toString" should "output like [Match,tableNaem]" in {
    val propertyGenerator = PropertyGenerator[Long]("path.to.generator",Seq(),Seq())
    val size = StaticValue[Long](10)
    val createPropertyTable = PropertyTable("typeName","propertyName",propertyGenerator,size)
    val graphGenerator = GraphGenerator("path.to.generator",Seq())
    val createEdgeTable = EdgeTable("tableName",graphGenerator,size)
    val match_ = Match("tableName",createPropertyTable,createEdgeTable)
    match_.toString should be ("[Match,tableName]")
  }
}
