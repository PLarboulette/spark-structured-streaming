
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{LongType, StringType, StructField, StructType}

object App  {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .appName("test1")
      .master("local")
      .getOrCreate()

    spark.sparkContext.setLogLevel("ERROR")


    import spark.implicits._
    import org.apache.spark.sql.functions._

    val schema = StructType(
      Seq(
        StructField("transactiontime", LongType, nullable = true),
        StructField("transationid", StringType, nullable = true),
        StructField("clientid", StringType, nullable = true),
        StructField("transactionamount", StringType, nullable = true)
      )
    )



    val kafkaDataFrame = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("subscribe", "transactions-json")
      .load()
      .selectExpr("CAST(value AS STRING) as message")
      .select(
        from_json(col("message"), schema).alias("parsed_value"))
      .select("parsed_value.*")


    kafkaDataFrame
      .writeStream
      .format("console")
      .option("truncate", value = false)
      .start()
      .awaitTermination()
  }
}
