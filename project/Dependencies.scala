import sbt._

object Dependencies {
  lazy val sparkVersion = "2.1.3"
  lazy val sparkCore = "org.apache.spark" %% "spark-core" % sparkVersion
  lazy val sparkSql = "org.apache.spark" %% "spark-sql" % sparkVersion
  lazy val scopt = "com.github.scopt" %% "scopt" % "3.7.1"
  lazy val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4"
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.2.10"
}
