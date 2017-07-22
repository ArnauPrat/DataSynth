package org.dama.datasynth.common.utils

import java.io.{FileInputStream, FileReader, IOException, InputStreamReader}

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}

import scala.util.matching.Regex

/**
  * Created by aprat on 19/07/17.
  */
object FileUtils {

  val hdfsRegex : Regex = "hdfs://(.*)".r
  val fileRegex : Regex = "file://(.*)".r
  val validRegex : Regex = "(file://|hdfs://)(/.*)".r

  def removePrefix( filename : String ) : String = {
    filename match {
      case hdfsRegex(path) => path
      case fileRegex(path) => path
      case _ => filename
    }
  }

  def isHDFS( filename : String ) : Boolean = {
    filename match {
      case hdfsRegex(path) => true
      case _ => false
    }
  }

  def isLocal( filename : String ) : Boolean = {
    filename match {
      case fileRegex(path) => true
      case _ => false
    }
  }

  def validateUri( filename : String ) = {
    filename match {
      case validRegex(_,filename) => Unit
      case _ => {
        throw new RuntimeException(s"Invalid URI: ${filename}. URIs must be " +
                                     s"prefixed with either file:// " +
                                     s"or hdfs:// and be an absolute path")
      }
    }
  }


  case class File( filename : String ) {
    def open() : Iterator[String] = {
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
