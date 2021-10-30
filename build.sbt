import Dependencies._

ThisBuild / organization := "gr.auth.csd.datalab"
ThisBuild / scalaVersion := "2.11.12"
ThisBuild / version      := "1.0"

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
    parallelExecution in Test := false,
    assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false),
    run in Compile := Defaults.runTask(
      fullClasspath in Compile,
      mainClass in(Compile, run),
      runner in(Compile, run)
    ).evaluated
  )
