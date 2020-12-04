import org.apache.spark.sql.{DataFrame, SparkSession}

import java.util.UUID

case class DataRow(clientId: Int, name: String, age: Int)

object ParquetConsumer {

  def main(args: Array[String]): Unit = {

    // It just be used to generate new files
    val uuid = UUID.randomUUID().toString

    val data = Seq(
      (1,"Batman", 34),
      (2,"Superman", 42),
      (3,"Wonder Woman", 25)
    )

    val spark = SparkSession
      .builder
      .appName("ParquetConsumer")
      .master("local")
      .getOrCreate()

    import spark.sqlContext.implicits._
    val dataSet = data.toDS()
    val dateFrame = dataSet.toDF("clientId","name","age")

    // Just writing the data in a temporary file
    dateFrame.write.parquet(s"/tmp/output/clients.parquet-$uuid")
    val read = spark.read.parquet(s"/tmp/output/clients.parquet-$uuid")
    read.createOrReplaceTempView("ParquetTable")
    val parkSQL = spark.sql("select * from ParquetTable where age <= 27")
      .select("clientId", "name", "age")

    parkSQL
      .write
      .format("console")
      .option("truncate", value = false)
      .save()

    // It should print the data for Wonder Woman
  }
}
