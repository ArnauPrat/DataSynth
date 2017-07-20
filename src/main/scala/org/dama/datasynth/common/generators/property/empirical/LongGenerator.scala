package org.dama.datasynth.common.generators.property.empirical

import java.io.InputStreamReader

import org.dama.datasynth.common.utils.FileUtils.File

/**
  * Created by aprat on 12/05/17.
  */
class LongGenerator(file : File, separator : String )
  extends DistributionBasedGenerator[Long](str => str.toLong, file, separator )
