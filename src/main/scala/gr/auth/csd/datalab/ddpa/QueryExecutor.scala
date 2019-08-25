package gr.auth.csd.datalab.ddpa

import gr.auth.csd.datalab.ddpa.config.QueryConfig
import gr.auth.csd.datalab.ddpa.schema.{Cell, Point, PointScore}
import org.apache.spark.sql.{Dataset, SparkSession}

class QueryExecutor(queryConfig: QueryConfig, spark: SparkSession) {

  private[this] val cellAttributesPerCellCalculator =
    new CellAttributesPerCellCalculator(
      queryConfig.dimensions,
      queryConfig.cellsPerDimension)

  private[this] val candidateCellFetcher =
    new CandidateCellFetcher(queryConfig.k)

  private[this] val candidatePointFetcher =
    new CandidatePointFetcher(queryConfig.k, spark)

  private[this] val topkPointFetcher =
    new TopkPointFetcher(
      queryConfig.k,
      queryConfig.dimensions,
      spark)

  def execute(inputPath: String): Seq[PointScore] = {
    val inputDataset = parseInput(inputPath).persist()
    val pointCountsPerCell = getPointCountsPerCell(inputDataset)

    val cellAttributesPerCell =
      cellAttributesPerCellCalculator.calculate(pointCountsPerCell)

    val candidateCells = candidateCellFetcher.fetch(cellAttributesPerCell)
    val bcCandidateCells = spark.sparkContext.broadcast(candidateCells)

    val candidatePoints =
      candidatePointFetcher.fetch(inputDataset, bcCandidateCells)

    topkPointFetcher.fetch(inputDataset, candidatePoints, bcCandidateCells)
  }

  private[this] def parseInput(inputPath: String): Dataset[Point] = {
    import spark.implicits._

    val bcCellWidth = spark.sparkContext.broadcast(queryConfig.cellWidth)
    val bcMinAllowedCoordinateValue =
      spark.sparkContext.broadcast(queryConfig.minAllowedCoordinateValue)

    spark
      .read
      .textFile(inputPath)
      .map(Point(_, bcCellWidth.value, bcMinAllowedCoordinateValue.value))
  }

  private[this] def getPointCountsPerCell(points: Dataset[Point]): Map[Cell, Long] = {
    import spark.implicits._
    points
      .groupByKey(_.parentCell)
      .count()
      .collect()
      .toMap
  }
}
