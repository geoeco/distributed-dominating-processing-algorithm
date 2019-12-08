package gr.auth.csd.datalab.ddpa

import gr.auth.csd.datalab.ddpa.schema.{Cell, CellAttributes, CellLowerBounds}

import scala.annotation.tailrec
import scala.collection.mutable

class CandidateCellFetcher(k: Int) {

  def fetch(cellAttributesPerCell: Map[Cell, CellAttributes]): Map[Cell, CellLowerBounds] = {

    val intermediateCandidateCells =
      cellAttributesPerCell
        .filter { case (_, cellAttributes) =>
          cellAttributes.lowerDominatedBound < k
        }

    // TODO: Make this cleaner.
    val kthPointLowerBoundScore =
      getKthPointLowerBoundScore(
        intermediateCandidateCells
          .map { case (_, cellAttributes) =>
            (cellAttributes.lowerDominatingBound, cellAttributes.pointCount)
          }
          .to[mutable.PriorityQueue])

    intermediateCandidateCells
      .flatMap { case (cell, cellAttributes) =>
        if (cellAttributes.upperDominatingBound >= kthPointLowerBoundScore)
          Some(cell ->
            CellLowerBounds(
              cellAttributes.lowerDominatingBound,
              cellAttributes.lowerDominatedBound))
        else None
      }
  }

  @tailrec private[this] def getKthPointLowerBoundScore(
    remainingCells: collection.mutable.PriorityQueue[(Long, Long)],
    currentLowerBound: Long = Long.MaxValue,
    pointAccumulator: Long = 0L): Long = {

    if (pointAccumulator >= k || remainingCells.isEmpty)
      currentLowerBound
    else {
      val (lowerDominatingBound, pointCount) = remainingCells.dequeue
      getKthPointLowerBoundScore(
        remainingCells,
        lowerDominatingBound,
        pointAccumulator + pointCount)
    }
  }
}