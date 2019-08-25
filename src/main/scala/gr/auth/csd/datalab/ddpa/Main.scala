package gr.auth.csd.datalab.ddpa

import java.io.{File, PrintWriter}

import gr.auth.csd.datalab.ddpa.config.{CommandLineConfig, QueryConfig}
import gr.auth.csd.datalab.ddpa.schema.PointScore
import org.apache.spark.sql.SparkSession

object Main {

  def main(args: Array[String]): Unit = {
    CommandLineConfig.parse(args) match {
      case Some(commandLineConfig) => run(commandLineConfig)
      case None => // arguments are bad, error message will be displayed
    }
  }

  def run(commandLineConfig: CommandLineConfig): Unit = {
    val spark = SparkSession.builder.getOrCreate()

    val queryConfig =
      QueryConfig(
        commandLineConfig.k,
        commandLineConfig.dimensions,
        commandLineConfig.cellsPerDimension,
        commandLineConfig.getCellWidth,
        commandLineConfig.minAllowedCoordinateValue)

    val queryExecutor = new QueryExecutor(queryConfig, spark)

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
