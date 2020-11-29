# spark-structured-streaming 

Little project to play with Spark Structured Streaming, Scala, Avro and Kafka 

## Tools

to run the project, you need Spark which can be download ["here](https://spark.apache.org/downloads.html)

I use also ksql-datagen. It's a tool available in the Confluent platform  which ben be download ["here](https://www.confluent.io/download/). I use the 
version 6.0.0 of the Confluent Platform (currently the last). 

Create some environment variables which point to the home folder of the project. I have `CONFLUENT_HOME` to use ksql-datagen and `SPARK_HOME` to use Spark. 


## How to run 

Clone the repository of this project. 

You need to run the Kafka cluster.
Use `docker-compose -f docker-compose.yml up`

## How tu run the JSON version 

To run the project, run : 
- `sbt package` . It will generate you a JAR which will be used by Spark. You can find it in the `target/scala-2.12` folder with the name `spark-structured-streaming_2.12-0.1.jar`.
- `$SPARK_HOME/bin/spark-submit --packages org.apache.spark:spark-sql-kafka-0-10_2.12:3.0.1 --class "JsonConsumer" --master local ./target/scala-2.12/spark-structured-streaming_2.12-0.1.jar`. The `JsonConsumer`corresponds to the name of the Scala Consumer located in the src folder. 
- After, we will generate some data with the ksql-datagen tool. 
- `$CONFLUENT_HOME/bin/ksql-datagen schema=schemas/transactions.avro format=json topic=transactions-json key=transationid maxInterval=1000`
- The `transactions.avro` schema will let to ksql-datagen to create data with a specified format.
- If you return on the window of Spark, you will see data appear. 

## How tu run the AVRO version 

To run the project, clone it and run : 
- `sbt assembly` . It will generate you a JAR which will be used by Spark. You can find it in the `target/scala-2.12` folder with the name `spark-structured-streaming.jar`.
- `$SPARK_HOME/bin/spark-submit --packages org.apache.spark:spark-sql-kafka-0-10_2.12:3.0.1 --class "AvroConsumer" --master local ./target/scala-2.12/spark-structured-streaming.jar`. The `AvroConsumer`corresponds to the name of the Scala Consumer located in the src folder. 
- After, we will generate some data with the ksql-datagen tool. 
- `$CONFLUENT_HOME/bin/ksql-datagen schema=schemas/transactions.avro format=avro topic=transactions-avro key=transationid maxInterval=1000`
- The `transactions.avro` schema will let to ksql-datagen to create data with a specified format.
- If you return on the window of Spark, you will see data appear. 




 
