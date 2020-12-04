#!/bin/bash

JARS="jars/avro-1.10.0.jar,jars/hadoop-common-3.3.0.jar,jars/hadoop-mapreduce-client-core-3.3.0.jar,jars/parquet-hadoop-1.11.1.jar,jars/parquet-common-1.11.1.jar,jars/parquet-column-1.11.1.jar,jars/parquet-format-2.8.0.jar,jars/parquet-encoding-1.11.1.jar,jars/parquet-avro-1.11.1.jar"

cd ..
sbt "clean; package"
"$SPARK_HOME"/bin/spark-submit  --jars $JARS --class "ParquetConsumer" --master local ./target/scala-2.12/spark-structured-streaming_2.12-0.1.jar
