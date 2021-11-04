package gr.auth.csd.datalab.ddpa

import gr.auth.csd.datalab.ddpa.models.{Cell, CellAttributes, CellLowerBounds}

import scala.annotation.tailrec
import scala.collection.mutable

class CandidateCellFetcher(k: Int) {

  def fetch(cellAttributesPerCell: Map[Cell, CellAttributes]): Map[Cell, CellLowerBounds] = {

    val intermediateCandidateCells = cellAttributesPerCell.filter { case (_, cellAttributes) =>
      cellAttributes.lowerDominatedBound < k
    }

    // TODO: Make this cleaner.
    val kthPointLowerBoundScore = getKthPointLowerBoundScore(
      intermediateCandidateCells
        .map { case (_, cellAttributes) =>
          (cellAttributes.lowerDominatingBound, cellAttributes.pointCount)
        }
        .to[mutable.PriorityQueue]
    )

    intermediateCandidateCells.collect {
      case (cell, cellAttributes)
        if cellAttributes.upperDominatingBound >= kthPointLowerBoundScore =>
        cell -> CellLowerBounds(
          cellAttributes.lowerDominatingBound,
          cellAttributes.lowerDominatedBound)
    }
  }

  @tailrec private def getKthPointLowerBoundScore(
    remainingCells: collection.mutable.PriorityQueue[(Long, Long)],
    currentLowerBound: Long = Long.MaxValue,
    pointAccumulator: Long = 0L
  ): Long =
    if (pointAccumulator >= k || remainingCells.isEmpty)
      currentLowerBound
    else {
      val (lowerDominatingBound, pointCount) = remainingCells.dequeue
      getKthPointLowerBoundScore(
        remainingCells,
        lowerDominatingBound,
        pointAccumulator + pointCount
      )
    }
}
