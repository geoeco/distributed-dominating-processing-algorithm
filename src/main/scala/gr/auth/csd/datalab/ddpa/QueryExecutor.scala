package gr.auth.csd.datalab.ddpa

import gr.auth.csd.datalab.ddpa.config.QueryConfig
import gr.auth.csd.datalab.ddpa.models.{Cell, Point, PointScore}
import org.apache.spark.sql.{Dataset, SparkSession}

class QueryExecutor(
    cellAttributesPerCellCalculator: CellAttributesPerCellCalculator,
    candidateCellFetcher: CandidateCellFetcher,
    candidatePointFetcher: CandidatePointFetcher,
    topkPointFetcher: TopkPointFetcher,
    queryConfig: QueryConfig
)(implicit spark: SparkSession) {

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

  private def parseInput(inputPath: String): Dataset[Point] = {
    import spark.implicits._

    val bcCellWidth = spark.sparkContext.broadcast(queryConfig.cellWidth)
    val bcMinAllowedCoordinateValue =
      spark.sparkContext.broadcast(queryConfig.minAllowedCoordinateValue)

    spark.read
      .textFile(inputPath)
      .map(Point(_, bcCellWidth.value, bcMinAllowedCoordinateValue.value))
  }

  private def getPointCountsPerCell(points: Dataset[Point]): Map[Cell, Long] = {
    import spark.implicits._
    points
      .groupByKey(_.parentCell)
      .count()
      .collect()
      .toMap
  }
}

object QueryExecutor {

  def apply(
      queryConfig: QueryConfig
  )(implicit spark: SparkSession): QueryExecutor =
    new QueryExecutor(
      new CellAttributesPerCellCalculator(
        queryConfig.dimensions,
        queryConfig.cellsPerDimension
      ),
      new CandidateCellFetcher(queryConfig.k),
      new CandidatePointFetcher(queryConfig.k),
      new TopkPointFetcher(queryConfig.k, queryConfig.dimensions),
      queryConfig
    )
}
