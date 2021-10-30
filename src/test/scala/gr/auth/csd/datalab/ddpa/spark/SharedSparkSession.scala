package gr.auth.csd.datalab.ddpa.spark

import org.apache.spark.sql.SparkSession
import org.scalatest.{BeforeAndAfterAll, Suite}

trait SharedSparkSession extends BeforeAndAfterAll {
  self: Suite =>

  lazy val spark: SparkSession =
    SparkSession
      .builder
      .master("local[*]")
      .config("spark.ui.enabled", "false")
      .getOrCreate()

  override def afterAll() {
    spark.stop()
  }
}
