name := "spark-structured-streaming"

version := "0.1"

lazy val commonSettings = Seq(
  version := "0.1-SNAPSHOT",
  organization := "plarboulette",
  scalaVersion := "2.12.12",
  test in assembly := {}
)

assemblyMergeStrategy in assembly := {
  case PathList("org","aopalliance", xs @ _*) => MergeStrategy.last
  case PathList("javax", "inject", xs @ _*) => MergeStrategy.last
  case PathList("javax", "servlet", xs @ _*) => MergeStrategy.last
  case PathList("javax", "activation", xs @ _*) => MergeStrategy.last
  case PathList("org", "apache", xs @ _*) => MergeStrategy.last
  case PathList("com", "google", xs @ _*) => MergeStrategy.last
  case PathList("com", "fasterxml", xs @ _*) => MergeStrategy.last
  case PathList("com", "esotericsoftware", xs @ _*) => MergeStrategy.last
  case PathList("com", "codahale", xs @ _*) => MergeStrategy.last
  case PathList("com", "yammer", xs @ _*) => MergeStrategy.last
  case "module-info.class" => MergeStrategy.discard
  case "about.html" => MergeStrategy.rename
  case "META-INF/ECLIPSEF.RSA" => MergeStrategy.last
  case "META-INF/mailcap" => MergeStrategy.last
  case "META-INF/mimetypes.default" => MergeStrategy.last
  case "plugin.properties" => MergeStrategy.last
  case "log4j.properties" => MergeStrategy.last
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

assemblyShadeRules in assembly := Seq(
  ShadeRule.rename("com.google.protobuf.**" -> "shadeproto.@1").inAll,
  ShadeRule.rename("scala.collection.compat.**" -> "shadecompat.@1").inAll
)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case "application.conf"            => MergeStrategy.concat
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

assemblyJarName in assembly := name.value + ".jar"

lazy val app = project
  .in(file("."))
  .settings(
    commonSettings,
    libraryDependencies  ++= Seq(
      "org.apache.kafka" % "kafka-clients" % "2.6.0",
      "org.apache.spark" %% "spark-sql-kafka-0-10" % "3.0.1" % "provided",
      "org.apache.spark" %% "spark-sql" % "3.0.1" % "provided",
      "com.thesamet.scalapb" %% "sparksql-scalapb" % "0.10.4",
      "io.confluent" % "kafka-avro-serializer" % "6.0.0",
      "io.confluent" % "kafka-schema-registry-client" % "6.0.0",
      "org.apache.spark" %% "spark-avro" % "3.0.1"
    ),
    resolvers ++= Seq(
      Resolver.jcenterRepo,
      "confluent" at "https://packages.confluent.io/maven/",
    )
  )
