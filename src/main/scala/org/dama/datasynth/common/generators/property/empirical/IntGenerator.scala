package org.dama.datasynth.common.generators.property.empirical

import java.io.InputStreamReader

import org.dama.datasynth.common.utils.FileUtils.File

/**
  * Created by aprat on 12/05/17.
  */
class IntGenerator( file : File, separator : String )
  extends DistributionBasedGenerator[Int]( str => str.toInt, file, separator )
