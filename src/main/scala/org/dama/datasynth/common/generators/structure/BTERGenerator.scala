package org.dama.datasynth.common.generators.structure
import ldbc.snb.bteronh.hadoop.HadoopBTERGenerator
import org.apache.hadoop.conf.Configuration
import org.dama.datasynth.common._

/**
  * Created by aprat on 20/04/17.
  *
  * Structure generator that implements the BTER generator, using the hadoop implementation
  * found in https://github.com/DAMA-UPC/BTERonH
  */
class BTERGenerator( degreesFile : utils.FileUtils.File,
                     ccsFile : utils.FileUtils.File ) extends StructureGenerator {

  override def run(num: Long, hdfsConf: Configuration, path: String): Unit = {

    val conf = new Configuration(hdfsConf)
    conf.setInt("ldbc.snb.bteronh.generator.numThreads", 4)
    conf.setLong("ldbc.snb.bteronh.generator.numNodes", num)
    conf.setInt("ldbc.snb.bteronh.generator.seed", 12323540)
    conf.set("ldbc.snb.bteronh.serializer.workspace", "hdfs:///tmp")
    conf.set("ldbc.snb.bteronh.serializer.outputFileName", path)
    conf.set("ldbc.snb.bteronh.generator.degreeSequence", degreesFile.filename)
    conf.set("ldbc.snb.bteronh.generator.ccPerDegree", ccsFile.filename)

    val generator = new HadoopBTERGenerator(conf)
    generator.run()
  }
}
