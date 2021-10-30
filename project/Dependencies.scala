import sbt._

object Dependencies {
  lazy val sparkVersion = "2.1.0"
  lazy val sparkCore = "org.apache.spark" %% "spark-core" % sparkVersion
  lazy val sparkSql = "org.apache.spark" %% "spark-sql" % sparkVersion
  lazy val scopt = "com.github.scopt" %% "scopt" % "3.7.0"
  lazy val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0"
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.8"
}
