package gr.auth.csd.datalab.ddpa

import gr.auth.csd.datalab.ddpa.implicits.CellConverter
import gr.auth.csd.datalab.ddpa.models.{BoundPair, Cell}

class DominatingBoundPairCalculator(cellsPerDimension: Int) {

  def calculate(
    cell: Cell,
    pointCount: Long,
    neighboringCellDominatingBounds: Map[Cell, BoundPair]
  ): BoundPair = {

    val lowerDominatingBound =
      if (cell.coordinates.contains(cellsPerDimension - 1))
        0L
      else {
        val closestFullyDominatedNeighbor = cell.coordinates.map(_ + 1).toCell
        neighboringCellDominatingBounds(closestFullyDominatedNeighbor).upper
      }

    val upperDominatingBound =
      getUpperDominatingBound(cell, pointCount, neighboringCellDominatingBounds)

    BoundPair(lowerDominatingBound, upperDominatingBound)
  }

  /** Calculates the upper dominating bound of the pivot cell by applying the inclusion-exclusion
    * principle.
    */
  private def getUpperDominatingBound(
    cell: Cell,
    pointCount: Long,
    neighboringCellBounds: Map[Cell, BoundPair]
  ): Long = {

    val dimensionsToCheck =
      cell.coordinates.zipWithIndex
        .collect {
          case (coordinate, dimension) if !(coordinate == cellsPerDimension - 1) => dimension
        }

    (1 to dimensionsToCheck.length)
      .foldLeft(0: Long) { (acc, n) =>
        val intersectionCardinalitySum =
          getIntersectionCardinalitySum(n, cell, neighboringCellBounds, dimensionsToCheck)

        if (n % 2 == 0)
          acc - intersectionCardinalitySum
        else
          acc + intersectionCardinalitySum
      } + pointCount
  }

  /** Calculates the sum of the cardinalities of the n-tuple-wise intersections. (e.g. If n = 2,
    * cardinalities of intersections consisting of 2 sets each are summed.)
    */
  private def getIntersectionCardinalitySum(
    n: Int,
    cell: Cell,
    neighboringCellBounds: Map[Cell, BoundPair],
    dimensionsToCheck: Seq[Int]
  ): Long = {

    dimensionsToCheck
      .combinations(n)
      .toList
      .foldLeft(0: Long) { (acc, combination) =>
        val neighborCell = cell.coordinates.zipWithIndex.map { case (coordinate, index) =>
          if (combination.contains(index))
            coordinate + 1
          else
            coordinate
        }.toCell

        acc + neighboringCellBounds(neighborCell).upper
      }
  }
}
