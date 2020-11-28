name := "spark-structured-streaming"

version := "0.1"

scalaVersion := "2.12.12"

lazy val commonSettings = Seq(
  version := "0.1-SNAPSHOT",
  organization := "plarboulette",
  scalaVersion := "2.10.1",
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

lazy val app = project
  .in(file("."))
  .settings(
    commonSettings,
    mainClass in assembly := Some("App"),
    assemblyJarName in assembly := "spark-streaming-example.jar",
    libraryDependencies  ++= Seq(
      "org.apache.kafka" % "kafka-clients" % "2.6.0",
      "org.apache.spark" %% "spark-sql-kafka-0-10" % "3.0.1" % "provided",
      "org.apache.spark" %% "spark-sql" % "3.0.1" % "provided"
    ),
    resolvers ++= Seq(
      Resolver.jcenterRepo,
      "confluent" at "https://packages.confluent.io/maven/",
    )
  )
