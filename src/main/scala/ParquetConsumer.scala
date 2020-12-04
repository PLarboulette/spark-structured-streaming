import org.apache.spark.sql.{DataFrame, SparkSession}

import java.util.UUID

case class DataRow(clientId: Int, name: String, age: Int)

object ParquetConsumer {

  def main(args: Array[String]): Unit = {

    // It just be used to generate new files
    val uuid = UUID.randomUUID().toString

    val dataHeroes = Seq(
      (1,"Batman", 34),
      (2,"Superman", 42),
      (3,"Wonder Woman", 25)
    )

    val dataCities = Seq(
      (1, "Metropolis", 2)
    )

    val spark = SparkSession
      .builder
      .appName("ParquetConsumer")
      .master("local")
      .getOrCreate()

    import spark.sqlContext.implicits._
    val dataHeroesSet = dataHeroes.toDS()
    val dataHeroesFrame = dataHeroesSet.toDF("id","name","age")

    val dataCitiesSet = dataCities.toDS()
    val dataCitiesFrame = dataCitiesSet.toDF("id", "name", "heroId")

    // Just writing the data in a temporary file
    dataHeroesFrame.write.parquet(s"/tmp/output/heroes.parquet-$uuid")
    dataCitiesFrame.write.parquet(s"/tmp/output/cities.parquet-$uuid")

    val readHeroes = spark.read.parquet(s"/tmp/output/heroes.parquet-$uuid")
    val readCities = spark.read.parquet(s"/tmp/output/cities.parquet-$uuid")


    readHeroes.createOrReplaceTempView("heroes")
    readCities.createOrReplaceTempView("cities")

    val sparkSQL = spark.sql("select * from heroes JOIN cities where age >= 41 AND heroes.id == cities.heroId")
      .select("heroes.id", "heroes.name", "heroes.age", "cities.name")

    sparkSQL
      .write
      .format("console")
      .option("truncate", value = false)
      .save()

    // It should print the data for SuperMan (age == 41 and city Metropolis)
  }
}
