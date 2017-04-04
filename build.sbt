name := "Scalaz and Scalaz Stream training notes"

version := "1.0"

scalaVersion := "2.11.8"

resolvers ++= Seq(
  "tpolecat" at "http://dl.bintray.com/tpolecat/maven",
  "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",
  "ifxjdbc" at "http://artifactory.cmdb.inhouse.paddypower.com:8081/artifactory/remote-repos",
  Resolver.sonatypeRepo("releases")
)

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.7.1")

val scalacheck = Seq(
    "org.scalacheck" %% "scalacheck" % "1.13.4"
)

val scalatest = Seq(
    "org.scalatest" %% "scalatest" % "3.0.1"
)

val doobie = Seq(
    "org.tpolecat" %% "doobie-core" % "0.2.2"
)

val informix = Seq(
    "com.ibm" % "ifxjdbc" % "3.70.JC3"
)

val akkaV = "2.4.4"

val akkaStreamV = "2.0-M2"

val scalazV = "7.1.12"

val scalazStreamV = "0.8"

val argonautV = "6.1"

val ammoniteV = "0.6.2"

val argonaut = Seq( 
  "io.argonaut" %% "argonaut" % argonautV
) 

val akka = Seq (
  "com.typesafe.akka" %% "akka-actor" % akkaV,
  "com.typesafe.akka" %% "akka-http-experimental" % akkaStreamV,
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaStreamV
)

val logging = Seq (
  "org.slf4j" % "slf4j-api" % "1.7.12",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  "com.typesafe.akka" %% "akka-slf4j" % akkaV
)

val scalaz = Seq(
  "org.scalaz" %% "scalaz-core" % scalazV
)

val scalazStream = Seq(
  "org.scalaz.stream" %% "scalaz-stream" % scalazStreamV
)

val ammonite = Seq(
  "com.lihaoyi" % "ammonite-repl" % ammoniteV % "test" cross CrossVersion.full
)

libraryDependencies ++= scalacheck ++ scalatest ++ scalaz ++ scalazStream

//initialCommands in console := "ammonite.repl.Main().run()"
