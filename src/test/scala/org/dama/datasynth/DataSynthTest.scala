package org.dama.datasynth

import java.io.File
import java.nio.file.{Files, Path, Paths}

import org.apache.commons.io.FileUtils
import org.apache.spark.sql.SparkSession
import org.junit.runner.RunWith
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner

import scala.util.{Failure, Success, Try}

/**
  * Created by aprat on 17/07/17.
  */
@RunWith(classOf[JUnitRunner])
class DataSynthTest extends FlatSpec with Matchers with BeforeAndAfterAll {


  " The test schema at /src/test/resources/test.json should work " should " work " in {

    SparkSession.builder().master("local[*]").getOrCreate()

    val testFolder = new File("./test")
    val dataFolder = new File("./test/data")
    val masterWorkspaceFolder = new File("./test/workspace")
    val datasynthWorkspaceFolder = new File("./test/workspace")
    testFolder.mkdir()
    dataFolder.mkdir()
    masterWorkspaceFolder.mkdir()
    val result = Try(DataSynth.main(List("--output-dir", "file://"+dataFolder.getAbsolutePath,
                                         "--master-workspace-dir", "file://"+masterWorkspaceFolder.getAbsolutePath,
                                         "--datasynth-workspace-dir", "file://"+datasynthWorkspaceFolder.getAbsolutePath,
                                         "--schema-file", "file://./src/test/resources/test.json").toArray))
    FileUtils.deleteDirectory(testFolder)
    result match {
      case Success(_) => Unit
      case Failure(error) => {
        throw new RuntimeException(error.getMessage)
      }
    }
  }
}
