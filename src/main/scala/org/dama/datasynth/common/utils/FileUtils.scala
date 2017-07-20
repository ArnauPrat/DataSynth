package org.dama.datasynth.common.utils

import java.io.{FileInputStream, FileReader, IOException, InputStreamReader}

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}

import scala.util.matching.Regex

/**
  * Created by aprat on 19/07/17.
  */
object FileUtils {

  case class File( filename : String ) {
    def open() : Iterator[String] = {
      val hdfsRegex : Regex = "hdfs://(.*)".r
      val fileRegex : Regex = "file://(.*)".r

      filename match {
        case hdfsRegex(path) => {
          val fileSystem = FileSystem.get(new Configuration())
          scala.io.Source.fromInputStream(fileSystem.open(new Path(path))).getLines()
        }
        case fileRegex(path) =>  {
          scala.io.Source.fromInputStream(new FileInputStream(new java.io.File(path))).getLines()
        }
        case _ => throw new IOException(s"Ill-formatted URI. ${filename} must start with hdfs:// or file://")
      }
    }
  }
}
