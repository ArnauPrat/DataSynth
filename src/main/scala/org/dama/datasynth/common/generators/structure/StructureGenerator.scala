package org.dama.datasynth.common.generators.structure

import org.apache.hadoop.conf.Configuration

import scala.util.Try

/**
  * Created by aprat on 20/04/17.
  */

object StructureGenerator {
  def getInstance( name : String ) : Try[StructureGenerator] = {
    Try(Class.forName(name).newInstance().asInstanceOf[StructureGenerator])
  }
}

abstract class StructureGenerator {
  def run( num : Long, hdfsConf : Configuration, path : String )
}
