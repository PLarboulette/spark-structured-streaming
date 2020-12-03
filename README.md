# spark-structured-streaming 

Little project to play with Spark Structured Streaming, Scala, Avro and Kafka 

## Tools

to run the project, you need Spark which can be download [here](https://spark.apache.org/downloads.html)

I use also ksql-datagen. It's a tool available in the Confluent platform  which ben be download [here](https://www.confluent.io/download/). I use the 
version 6.0.0 of the Confluent Platform (currently the last). 

Create some environment variables which point to the home folder of the project. I have `CONFLUENT_HOME` to use ksql-datagen and `SPARK_HOME` to use Spark. 


## How to run 

Clone the repository of this project. 

You need to run the Kafka cluster.
Use `docker-compose -f docker-compose.yml up`

## How to run the JSON version 

To run the project, run : 
- `sbt package` . It will generate you a JAR which will be used by Spark. You can find it in the `target/scala-2.12` folder with the name `spark-structured-streaming_2.12-0.1.jar`.
- `$SPARK_HOME/bin/spark-submit --packages org.apache.spark:spark-sql-kafka-0-10_2.12:3.0.1 --class "JsonConsumer" --master local ./target/scala-2.12/spark-structured-streaming_2.12-0.1.jar`. The `JsonConsumer`corresponds to the name of the Scala Consumer located in the src folder. 
- After, we will generate some data with the ksql-datagen tool. 
- `$CONFLUENT_HOME/bin/ksql-datagen schema=schemas/transactions.avro format=json topic=transactions-json key=transationid iterations=1000`
- The `transactions.avro` schema will let to ksql-datagen to create data with a specified format.
- If you return on the window of Spark, you will see data appear. 

## How tu run the AVRO version 

To run the project, clone it and run : 
- `sbt assembly` . It will generate you a JAR which will be used by Spark. You can find it in the `target/scala-2.12` folder with the name `spark-structured-streaming.jar`.
- First, we need to generate some data with the ksql-datagen tool to create the schemas in the Registry. The Avro consumer and Spark need them to correctly run. 
- `$CONFLUENT_HOME/bin/ksql-datagen schema=schemas/transactions.avro format=avro topic=transactions-avro key=transactionid iterations=1`
- `$CONFLUENT_HOME/bin/ksql-datagen schema=schemas/clients.avro format=avro topic=clients-avro key=clientid iterations=1`
- Now, we can launch the consumer 
- `$SPARK_HOME/bin/spark-submit --packages org.apache.spark:spark-sql-kafka-0-10_2.12:3.0.1 --class "AvroConsumer" --master local ./target/scala-2.12/spark-structured-streaming.jar`. The `AvroConsumer`corresponds to the name of the Scala Consumer located in the src folder. 
- Because of the `latest` configuration of the consumer, it will no re-read previously pushed events, so you have to re-launch the two commands of ksql-datagen. 
- The Avro consumer read two different sources (or topics) : clients and transactions. The two topics are linked by a `clientId`field. 
I configure a kind of `join function` between the two sources, on the clientId field. 
- If you return on the window of Spark, you will see data appear, but only when the transaction.clientId = client.clientId :)
- if you look the code, you can see a `dropDuplicates` function. We need this function to avoid the streams to become too big. Without it, it can wait 
a long time to have two linked entities. I choose to just drop the previous entry, if two clients have the same Ids, the old one is dropped. (it not the best behaviour but it's just a test, I will work to improve that)
- You have another way to do it, with a kind of `eventTime` field. You put as value the moment when you push the event, and if the event is too old, it will be remove of the stream. I think it's a better way of do what I want, I will work on that. 

## Scripts 

Some scripts are available in the `scripts` folder. For example, to add transactions for the JSON consumer, you can use (since the `scripts` folder) : 
 - `sh transactions.sh json` 

For Avro consumer, just replace `json` by `avro`. 


## How tu run the PostgreSQL version
- First, we need the PostgreSQL driver (can be found [here](https://jdbc.postgresql.org/download.html)). I use in this repository the 42.2.18 version.
- I put the JAR at the root of the repository
- After, I use a tool called [pgcli](https://www.pgcli.com/) to create my table and to insert data. The command lines can be found in the script folder. 
- After, go to script and launch `execute-postgresql-consumer.sh`. 
