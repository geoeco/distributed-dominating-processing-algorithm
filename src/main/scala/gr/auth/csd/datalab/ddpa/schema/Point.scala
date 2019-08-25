package gr.auth.csd.datalab.ddpa.schema

import gr.auth.csd.datalab.ddpa.implicits.CellConverter

case class Point(coordinates: Seq[Double], parentCell: Cell) {

  def dominates(that: Point): Boolean = {
    val coordinatePairs = this.coordinates.zip(that.coordinates)

    val dominatesAtLeastOneDimension =
      coordinatePairs
        .exists { case (thisCoordinate, thatCoordinate) =>
          thisCoordinate < thatCoordinate
        }

    val notDominatedInAnyDimension =
      !coordinatePairs
        .exists { case (thisCoordinate, thatCoordinate) =>
          thisCoordinate > thatCoordinate
        }

    dominatesAtLeastOneDimension && notDominatedInAnyDimension
  }
}

object Point {

  def apply(
    rawPoint: String,
    cellWidth: Double,
    minAllowedCoordinateValue: Double): Point = {

    val pointCoordinates =
      rawPoint
        .split(",")
        .map(_.toDouble)
        .toSeq

    val parentCell =
      pointCoordinates
        .map(coordinate =>
          Math.floor((coordinate - minAllowedCoordinateValue) / cellWidth).toInt)
        .toCell

    Point(pointCoordinates, parentCell)
  }
}