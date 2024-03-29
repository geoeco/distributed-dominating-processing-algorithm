package gr.auth.csd.datalab.ddpa

import gr.auth.csd.datalab.ddpa.implicits.CellConverter
import gr.auth.csd.datalab.ddpa.models.{BoundPair, Cell}
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class DominatingBoundPairCalculatorSpec extends AnyWordSpec with Matchers {

  import DominatingBoundPairCalculatorSpec._

  private val dominatingBoundPairCalculator = new DominatingBoundPairCalculator(cellsPerDimension)

  "DominatingBoundPairCalculator" must {
    "calculate the dominating bound pair of a cell in the middle of the grid" in {
      val cell = Seq(1, 1, 1).toCell
      val pointCount = 7
      val neighboringCellDominatingBounds = Map(
        Seq(1, 1, 2).toCell -> BoundPair(0, 26),
        Seq(1, 2, 1).toCell -> BoundPair(0, 30),
        Seq(1, 2, 2).toCell -> BoundPair(0, 15),
        Seq(2, 1, 1).toCell -> BoundPair(0, 38),
        Seq(2, 1, 2).toCell -> BoundPair(0, 18),
        Seq(2, 2, 1).toCell -> BoundPair(0, 19),
        Seq(2, 2, 2).toCell -> BoundPair(0, 10)
      )

      val actual =
        dominatingBoundPairCalculator.calculate(cell, pointCount, neighboringCellDominatingBounds)
      val expected = BoundPair(10, 59)

      actual mustBe expected
    }

    "calculate the dominating bound pair of a cell in the  edge of the grid for all dimensions" in {
      val cell = Seq(2, 2, 2).toCell
      val pointCount = 10
      val neighboringCellDominatingBounds = Map.empty[Cell, BoundPair]

      val actual =
        dominatingBoundPairCalculator.calculate(cell, pointCount, neighboringCellDominatingBounds)
      val expected = BoundPair(0, 10)

      actual mustBe expected
    }

    "calculate the dominating bound pair of a cell in the edge of the grid for one dimension" in {
      val cell = Seq(2, 2, 1).toCell
      val pointCount = 9
      val neighboringCellDominatingBounds = Map(Seq(2, 2, 2).toCell -> BoundPair(0, 10))

      val actual =
        dominatingBoundPairCalculator.calculate(cell, pointCount, neighboringCellDominatingBounds)
      val expected = BoundPair(0, 19)

      actual mustBe expected
    }
  }
}

object DominatingBoundPairCalculatorSpec {
  val cellsPerDimension = 3
}
