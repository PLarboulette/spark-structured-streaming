#!/bin/bash

cd ..
sbt package
"$SPARK_HOME"/bin/spark-submit --packages org.apache.spark:spark-sql-kafka-0-10_2.12:3.0.1 --class "JsonConsumer" --master local ./target/scala-2.12/spark-structured-streaming_2.12-0.1.jar