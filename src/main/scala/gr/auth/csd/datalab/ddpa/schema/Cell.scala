package gr.auth.csd.datalab.ddpa.schema

case class Cell(coordinates: Seq[Int]) {

  def partiallyDominates(that: Cell): Boolean = {
    val coordinatePairs = this.coordinates.zip(that.coordinates)

    val dominates =
      !coordinatePairs
        .exists { case (thisCoordinate, thatCoordinate) =>
          thisCoordinate > thatCoordinate
        }

    val doesNotFullyDominate =
      coordinatePairs
        .exists { case (thisCoordinate, thatCoordinate) =>
          thisCoordinate == thatCoordinate
        }

    dominates && doesNotFullyDominate
  }

  def mirrorCoordinates(cellsPerDimension: Int): Cell =
    Cell(coordinates.map(i => -i + cellsPerDimension - 1))
}

object Cell {

  def apply(cellId: Long, dimensions: Int, cellsPerDimension: Int): Cell = {
    val cellCoordinates =
      (0 until dimensions)
        .foldRight((cellId, List[Int]())) { case (dimension, (previousRemainder, acc)) =>
          val divisor = Math.pow(cellsPerDimension, dimension)
          ((previousRemainder % divisor).toInt,
            (previousRemainder / divisor).toInt :: acc)
        }
        ._2
        .reverse

    Cell(cellCoordinates)
  }
}
