name := "distributed-dominating-processing-algorithm"

organization := "gr.auth.csd.datalab"

version := "1.0"

scalaVersion := "2.11.12"

val sparkVersion = "2.1.0"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % Provided,
  "org.apache.spark" %% "spark-sql" % sparkVersion % Provided,
  "com.github.scopt" %% "scopt" % "3.7.0",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0",
  "org.scalactic" %% "scalactic" % "3.0.8",
  "org.scalatest" %% "scalatest" % "3.0.8" % Test)

parallelExecution in Test := false

assemblyOption in assembly := 
  (assemblyOption in assembly).value.copy(includeScala = false)

run in Compile :=
  Defaults
    .runTask(
      fullClasspath in Compile,
      mainClass in(Compile, run),
      runner in(Compile, run))
    .evaluated