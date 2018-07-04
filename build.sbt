ThisBuild / scalaVersion := "2.12.6"
ThisBuild / organization := "com.doleglo.kamil"

lazy val procrastination = (project in file("."))
  .settings(
    name := "Procrastination",
    resolvers += Resolver.bintrayRepo("cakesolutions", "maven"),
    libraryDependencies += "org.bytedeco" % "javacv-platform" % "1.4.1",
    libraryDependencies += "net.cakesolutions" %% "scala-kafka-client" % "1.1.0",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % "2.5.13",
      "com.typesafe.akka" %% "akka-testkit" % "2.5.13" % Test
    ),
    libraryDependencies += "net.cakesolutions" %% "scala-kafka-client-akka" % "1.1.0"
  )

