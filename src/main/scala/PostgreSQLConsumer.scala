import org.apache.spark.sql.SparkSession

object PostgreSQLConsumer {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession
      .builder
      .appName("test3")
      .master("local")
      .getOrCreate()

    spark.sparkContext.setLogLevel("ERROR")

    // The driver JDBC doesn't support the streams :/
    val jdbcDF = spark.read
      .format("jdbc")
      .option("url", "jdbc:postgresql://localhost/test?user=test&password=test")
      .option("dbtable", "clients") //  My table
      .option("user", "test")
      .option("password", "test")  // Amazing credentials
      .load()
      .select("clientId", "name")

    jdbcDF.write
      .format("console")
      .option("truncate", value = false)
      .save()
  }
}
