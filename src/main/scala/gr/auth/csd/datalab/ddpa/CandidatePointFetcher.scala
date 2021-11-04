package gr.auth.csd.datalab.ddpa

import gr.auth.csd.datalab.ddpa.models.{Cell, CellLowerBounds, Point}
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.sql.{Dataset, SparkSession}

class CandidatePointFetcher(k: Int)(implicit spark: SparkSession) {

  import CandidatePointFetcher._

  def fetch(
    inputDataset: Dataset[Point],
    candidateCells: Broadcast[Map[Cell, CellLowerBounds]]
  ): Seq[Point] = {

    import spark.implicits._

    val pointsInCandidateCells =
      inputDataset
        .filter(point => candidateCells.value.contains(point.parentCell))
        .persist()

    val bcPointsInCandidateCells =
      spark.sparkContext.broadcast(pointsInCandidateCells.collect().toList)
    val bcK = spark.sparkContext.broadcast(k)

    val candidatePoints = pointsInCandidateCells
      .flatMap(point =>
        pruneIfDominatedByAtLeastK(
          point,
          bcK.value,
          candidateCells.value(point.parentCell).lowerDominated,
          bcPointsInCandidateCells.value))
      .collect()
      .toList

    pointsInCandidateCells.unpersist()
    bcPointsInCandidateCells.destroy()
    bcK.destroy()

    candidatePoints
  }
}

object CandidatePointFetcher {

  def pruneIfDominatedByAtLeastK(
    point: Point,
    k: Int,
    initialDominatedCount: Long,
    otherPoints: Seq[Point]
  ): Option[Point] = {

    val dominatedCount =
      otherPoints.foldLeft(initialDominatedCount) { (currentDominatedCount, otherPoint) =>
        if (
          otherPoint.parentCell.partiallyDominates(point.parentCell) && otherPoint.dominates(point)
        )
          currentDominatedCount + 1L
        else
          currentDominatedCount
      }

    if (dominatedCount >= k) None
    else Some(point)
  }
}
