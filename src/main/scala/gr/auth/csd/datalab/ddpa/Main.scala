package gr.auth.csd.datalab.ddpa

import gr.auth.csd.datalab.ddpa.config.{CommandLineConfig, QueryConfig}
import gr.auth.csd.datalab.ddpa.models.PointScore
import org.apache.spark.sql.SparkSession

import java.io.{File, PrintWriter}

object Main {

  def main(args: Array[String]): Unit = {
    CommandLineConfig.parse(args) match {
      case Some(commandLineConfig) => run(commandLineConfig)
      case None => // arguments are bad, error message will be displayed
    }
  }

  def run(commandLineConfig: CommandLineConfig): Unit = {
    implicit val spark = SparkSession.builder.getOrCreate()

    val queryConfig = QueryConfig.fromCommandLineConfig(commandLineConfig)
    val queryExecutor = QueryExecutor(queryConfig)

    val topkPoints = queryExecutor.execute(commandLineConfig.inputPath)
    writeOutput(topkPoints, commandLineConfig.outputDir)

    spark.stop()
  }

  def writeOutput(topkPoints: Seq[PointScore], outputDir: String): Unit = {
    val pw = new PrintWriter(new File(outputDir + "/query_result.txt"))
    topkPoints.foreach(pointScore => pw.write(s"${pointScore.toString}\n"))
    pw.close()
  }
}
