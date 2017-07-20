package org.dama.datasynth.common.generators.structure

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.dama.datasynth.common._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

/**
  * Created by aprat on 20/04/17.
  */

@RunWith(classOf[JUnitRunner])
class BTERGeneratorTest extends FlatSpec with Matchers with BeforeAndAfterAll {

  "BTERGenerator " should "not crash and produce a graph in /tmp/bterEdges" in {
    val bterGenerator = new BTERGenerator(utils.FileUtils.File("file://./src/main/resources/degrees/dblp"),
                             utils.FileUtils.File("file://./src/main/resources/ccs/dblp"));
    bterGenerator.run(1000000, new Configuration(), "hdfs:///tmp/bterEdges")
    val fileSystem = FileSystem.get(new Configuration())
    fileSystem.exists(new Path("/tmp/bterEdges")) should be (true)
  }

  override protected def afterAll(): Unit = {
    val fileSystem = FileSystem.get(new Configuration())
    fileSystem.delete(new Path("/tmp/bterEdges"), true)
  }
}
