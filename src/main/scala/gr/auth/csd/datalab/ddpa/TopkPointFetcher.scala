package gr.auth.csd.datalab.ddpa

import gr.auth.csd.datalab.ddpa.models.{
  Cell,
  CellLowerBounds,
  Point,
  PointScore
}
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.sql.{Dataset, SparkSession}

class TopkPointFetcher(k: Int, dimensions: Int)(implicit spark: SparkSession) {

  import TopkPointFetcher._

  def fetch(
      inputDataset: Dataset[Point],
      candidatePoints: Seq[Point],
      candidateCells: Broadcast[Map[Cell, CellLowerBounds]]
  ): Seq[PointScore] = {

    import spark.implicits._
    val trimmedInputDataset = trimInputDataset(inputDataset, candidatePoints)
    val bcCandidatePoints = spark.sparkContext.broadcast(candidatePoints)

    val candidatePointScores =
      trimmedInputDataset
        .flatMap(
          getDominatingCandidatesFromPartiallyDominatingCells(
            _,
            bcCandidatePoints.value
          )
        )
        .groupByKey(candidate => candidate)
        .count()
        .map { case (candidate, partialScore) =>
          val score = partialScore + candidateCells
            .value(candidate.parentCell)
            .lowerDominating
          PointScore(candidate.coordinates, score)
        }

    val topkDominatingPoints = getTopK(candidatePointScores)

    bcCandidatePoints.destroy()

    topkDominatingPoints
  }

  /** Trims the original dataset of all the points that are not required for
    * the calculation of the candidate point scores, as they are already
    * included in the lower dominating bound of ALL the candidates' parent cells.
    */
  private def trimInputDataset(
      inputDataset: Dataset[Point],
      candidates: Seq[Point]
  ): Dataset[Point] = {

    val maxRequiredCellCoordinatePerDimension =
      candidates
        .foldLeft(Seq.fill(dimensions)(0)) { (acc, candidate) =>
          candidate.parentCell.coordinates
            .zip(acc)
            .map { case (cellCoordinate, currentMaxRequiredCoordinate) =>
              Math.max(cellCoordinate, currentMaxRequiredCoordinate)
            }
        }

    val bcMaxRequiredCellCoordinatePerDimension =
      spark.sparkContext.broadcast(maxRequiredCellCoordinatePerDimension)

    inputDataset
      .filter(
        _.parentCell.coordinates
          .zip(bcMaxRequiredCellCoordinatePerDimension.value)
          .exists { case (cellCoordinate, maxRequiredCoordinate) =>
            cellCoordinate <= maxRequiredCoordinate
          }
      )
  }

  private def getTopK(
      candidatePointScores: Dataset[PointScore]
  ): Seq[PointScore] = {
    import spark.implicits._
    val bcK = spark.sparkContext.broadcast(k)
    val topK = candidatePointScores
      .orderBy('score.desc)
      .limit(bcK.value)
      .collect()
      .toList

    bcK.destroy()
    topK
  }
}

object TopkPointFetcher {

  def getDominatingCandidatesFromPartiallyDominatingCells(
      point: Point,
      candidates: Seq[Point]
  ): Seq[Point] = candidates.flatMap { candidatePoint =>
    if (
      candidatePoint.parentCell.partiallyDominates(point.parentCell)
      && candidatePoint.dominates(point)
    ) Some(candidatePoint)
    else None
  }
}
