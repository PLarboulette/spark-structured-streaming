#!/bin/bash

cd ..
sbt assembly
"$SPARK_HOME"/bin/spark-submit --packages org.apache.spark:spark-sql-kafka-0-10_2.12:3.0.1 --class "AvroConsumer" --master local ./target/scala-2.12/spark-structured-streaming.jar