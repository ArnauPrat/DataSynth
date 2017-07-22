package org.dama.datasynth

/**
  * Created by aprat on 3/05/17.
  *
  * Class used to store the configuration of DataSynth
  *
  */

object DataSynthConfig {
  def apply() : DataSynthConfig = {
    new DataSynthConfig()
  }

  def apply( args : List[String] ) : DataSynthConfig = {
    nextOption( new DataSynthConfig(), args)
  }

  /**
    * Parses the next option from the option list
    * @param currentConfig The current DataSynth config
    * @param list The list of remaining options to parse
    * @return The new DataSynth config
    */
  def nextOption(currentConfig : DataSynthConfig, list: List[String]) : DataSynthConfig = {
    def isSwitch(s : String) = (s(0) == '-')
    list match {
      case "--output-dir" :: outputdir :: tail if !isSwitch(outputdir) => {
        val config = currentConfig.setOutputDir(outputdir)
        nextOption(config, tail)
      }
      case "--schema-file" :: schema :: tail if !isSwitch(schema) => {
        val config = currentConfig.schemaFile(schema)
        nextOption(config, tail)
      }
      case "--master-workspace-dir" :: workspace :: tail if !isSwitch(workspace) => {
        val config = currentConfig.masterWorkspaceDir(workspace)
        nextOption(config, tail)
      }
      case "--datasynth-workspace-dir" :: workspace :: tail if !isSwitch(workspace) => {
        val config = currentConfig.datasynthWorkspaceDir(workspace)
        nextOption(config, tail)
      }
      case option :: tail => {
        throw new Exception(s"Unknown option $option")
      }
      case Nil => currentConfig
    }
  }

  def validateConfig( config : DataSynthConfig ) = {
    if(config.outputDir.equals("") ){
      throw new RuntimeException(s"Output dir not specified. Use --output-dir <path> option")
    }

    if(config.schemaFile.equals("") ){
      throw new RuntimeException(s"Schema file not specified. Use --schema-file <path> option")
    }
  }
}

class DataSynthConfig ( val outputDir : String = "",
                        val schemaFile : String = "",
                        val masterWorkspaceDir : String = "file:///tmp",
                        val datasynthWorkspaceDir : String = "file:///tmp")
{

  /**
    * Sets the outputDir
    * @param newOutputDir The value of the output dir
    * @return this
    */
  def setOutputDir(newOutputDir : String ) : DataSynthConfig = {
    new DataSynthConfig(newOutputDir,
                        schemaFile,
                        masterWorkspaceDir,
                        datasynthWorkspaceDir)
  }

  /**
    * Sets the schema file path
    * @param newSchemaFile The value of the schema file path
    * @return this
    */
  def schemaFile(newSchemaFile : String ) : DataSynthConfig = {
    new DataSynthConfig(outputDir,
                        newSchemaFile,
                        masterWorkspaceDir,
                        datasynthWorkspaceDir)
  }

  /**
    * Sets the master workspace dir
    * @param newWorkspace The value of the driver workspace dir
    * @return this
    */
  def masterWorkspaceDir(newWorkspace: String ) : DataSynthConfig = {
    if(!common.utils.FileUtils.isLocal(newWorkspace)) {
      throw new RuntimeException(s"Invalid master workspace directory ${newWorkspace}." +
                                   s" Master workspace directory must be in local file" +
                                   s" system and thus prefixed with file://")
    }
    new DataSynthConfig(outputDir,
                        schemaFile,
                        newWorkspace,
                        datasynthWorkspaceDir)
  }

  /**
    * Sets datasynth's workspace dir
    * @param newWorkspace The value of the driver workspace dir
    * @return this
    */
  def datasynthWorkspaceDir(newWorkspace: String ) : DataSynthConfig = {
    new DataSynthConfig(outputDir,
                        schemaFile,
                        masterWorkspaceDir,
                        newWorkspace)
  }
}
