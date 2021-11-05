package gr.auth.csd.datalab.ddpa

import gr.auth.csd.datalab.ddpa.models.{BoundPair, Cell, CellAttributes}

class CellAttributesPerCellCalculator(dimensions: Int, cellsPerDimension: Int) {

  private val dominatingBoundPairCalculator = new DominatingBoundPairCalculator(cellsPerDimension)
  private val cellIdRange = 0L until Math.pow(cellsPerDimension, dimensions).toLong

  def calculate(pointCountsPerCell: Map[Cell, Long]): Map[Cell, CellAttributes] = {
    val dominatingBoundsPerCell = getDominatingBoundsPerCell(pointCountsPerCell)
    val dominatedBoundsPerCell = getDominatedBoundsPerCell(pointCountsPerCell)

    pointCountsPerCell.map {
      case (cell, pointCount) =>
        val dominatingBoundPair = dominatingBoundsPerCell(cell)
        val dominatedBoundPair = dominatedBoundsPerCell(cell)
        cell -> CellAttributes(
          pointCount,
          dominatingBoundPair.lower,
          dominatingBoundPair.upper,
          dominatedBoundPair.lower
        )
    }
  }

  private def getDominatingBoundsPerCell(
    pointCountsPerCell: Map[Cell, Long]
  ): Map[Cell, BoundPair] =
    cellIdRange.foldRight(Map[Cell, BoundPair]()) { (cellId, acc) =>
      val cell = Cell(cellId, dimensions, cellsPerDimension)
      val pointCount = pointCountsPerCell.getOrElse(cell, 0L)
      val dominatingBoundPair = dominatingBoundPairCalculator.calculate(cell, pointCount, acc)
      acc.updated(cell, dominatingBoundPair)
    }

  private def getDominatedBoundsPerCell(
    pointCountsPerCell: Map[Cell, Long]
  ): Map[Cell, BoundPair] = {
    val mirroredPointCounts = pointCountsPerCell.map {
      case (cell, pointCount) => cell.mirrorCoordinates(cellsPerDimension) -> pointCount
    }

    getDominatingBoundsPerCell(mirroredPointCounts).map {
      case (cell, boundPair) => cell.mirrorCoordinates(cellsPerDimension) -> boundPair
    }
  }
}
