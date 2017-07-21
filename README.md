# DataSynth
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/13e27d1053af4b2ab53414618b858fdc)](https://www.codacy.com/app/ArnauPrat/DataSynth?utm_source=github.com&utm_medium=referral&utm_content=DAMA-UPC/DataSynth&utm_campaign=badger)
[![Build Status](https://travis-ci.org/DAMA-UPC/DataSynth.svg?branch=dev)](https://travis-ci.org/DAMA-UPC/DataSynth)

DataSynth is a tool for the generation of user-driven datasets with arbitrary and complex schemas. Schemas are defined using the property graph model, where data is modeled as a graph with nodes representing the different entity types and edges represent the relations among them. In this model, both nodes and edges can have attached properties, in the form of key value pairs. Among many novel features, DataSynth has the following characteristics:

* Allows specifying multiple nodes and edge types, each type with its own set of properties.
* Allows full control of the way properties are generated, including the specification of correlations between those of the same entity and connected entities.
* Allows controlling structural properties of the underlying network of edges via the available structure generators.
* Allows scaling to terabytes of data, thanks to the use of BigData technologies such as Apache Spark.

The core idea of DataSynth, was first described in detail in the paper:

[Towards a property graph generator for benchmarking](https://arxiv.org/abs/1704.00630)

Arnau Prat-Pérez, Joan Guisado-Gámez, Xavier Fernández Salas, Petr Koupy, Siegfried Depner, Davide Basilio Bartolini

## Installing

We use Maven as our build tool. To compile the project, just type the following command in the project's root folder:
```
mvn -DskipTests assembly:assembly
```
Additionally, DataSynth requires a working installation of [Apache Spark](http://spark.apache.org) 2.0.1, compiled for your Hadoop version 

## Running DataSynth

DataSynth uses Apache Spark to perform de generation of data. As a Spark application, it is executed using the spark-submit script provided by spark. From DataSynth's root folder, execute the following command:
```
$SPARK_HOME/bin/spark-submit -v --master local[*] --class org.dama.datasynth.DataSynth target/datasynth-1.0-SNAPSHOT-jar-with-dependencies.jar --schema-file file://./src/main/resources/examples/example.json  --output-dir file://./datasynth
```
The <kbd>--output-dir</kbd> option specifies the folder where the generated dataset will be placed, while the <kbd>--schema-file</kbd> specifies the schema of the graph to generate. Prefixing paths with "file://" or "hdfs://" is required. The example.json schema file defines the following schema:

```json
{
  "nodeTypes" : [ 
    {   
      "name" : "person",
      "instances" : 1000000,
      "properties" : [ 
        {
          "name": "attribute1",
          "dataType": "Int",
          "generator": {
            "name":"org.dama.datasynth.common.generators.property.empirical.IntGenerator",
            "dependencies":[],
            "initParameters" : ["file://./src/main/resources/distributions/intDistribution.txt:File"," :String"]}
        }
      ]   
    }   
  ],  
  "edgeTypes" : [ 
    {   
      "name" : "knows",
      "source" : "person",
      "target" : "person",
      "structure" : { 
        "name" : "org.dama.datasynth.common.generators.structure.BTERGenerator",
        "initParameters" : ["file://./src/main/resources/degrees/dblp:File","file://./src/main/resources/ccs/dblp:File"]
      }   
    }   
  ]
}
```
Currently, the schema is specified in a rather low level json, although we plan to release a Domain Specific Language for convenience. The above schema specifies the generation of 1000000 entities of type Person, which contain an Integer attribute. Such attribute is generated with the property generator "org.dama.datasynth.common.generators.property.empirical.IntGenerator". 

For now, a Property Generator is a class responsible of generating the values of an attribute for a given entity. The "initParameters" field specifies the required parameters for initializing the generator and their types. In this case, we pass a pointer to a file containing the distribution of the integer values to generate.

The schema also specifies the generation of an edge type with name "knows", which connects paris of persons. The edge is generated with a Structure Generator, which is the responsible of generating the graph connecting the nodes. In this case, we use a BTER graph generator, which takes the degree distribution and the average clustering coefficient per degree as parameters.


## Contributing

Feel free to contribute to the project by issuing pull requests, suggestions
etc. A Trello board with the current pending and in-dev tasks can be found here:

https://trello.com/b/AEZ99vTz/datasynth

## Citing this Work

```
@article{prat2017towards,
  title={Towards a property graph generator for benchmarking},
  author={Prat-P{\'e}rez, Arnau and Guisado-G{\'a}mez, Joan and Salas, Xavier Fern{\'a}ndez and Koupy, Petr and Depner, Siegfried and Bartolini, Davide Basilio},
  journal={arXiv preprint arXiv:1704.00630},
  year={2017}
}
```

