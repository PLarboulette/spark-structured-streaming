
#!/bin/bash

cd ..
sbt "clean; package"
"$SPARK_HOME"/bin/spark-submit --driver-class-path postgresql-42.2.18.jar --jars postgresql-42.2.18.jar --class "PostgreSQLConsumer" --master local ./target/scala-2.12/spark-structured-streaming_2.12-0.1.jar