import Dependencies._

ThisBuild / organization := "gr.auth.csd.datalab"
ThisBuild / scalaVersion := "2.11.12"
ThisBuild / version := "1.0"

lazy val root = (project in file("."))
  .settings(
    name := "distributed-dominating-processing-algorithm",
    libraryDependencies ++= Seq(
      sparkCore % Provided,
      sparkSql % Provided,
      scopt,
      scalaLogging,
      scalaTest % Test
    ),
    Test / parallelExecution := false,
    assembly / assemblyOption := (assembly / assemblyOption)
      .value
      .copy(includeScala = false),
    Compile / run := Defaults
      .runTask(
        Compile / fullClasspath,
        Compile / run / mainClass,
        Compile / run / runner
      )
      .evaluated
  )
