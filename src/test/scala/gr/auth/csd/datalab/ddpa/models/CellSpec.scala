package gr.auth.csd.datalab.ddpa.models

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CellSpec extends AnyWordSpec with Matchers {

  "partiallyDominates method" must {
    "return true if cell x partially dominates cell y" in {
      val x = Cell(Seq(1, 2, 1))
      val y = Cell(Seq(2, 2, 2))

      x.partiallyDominates(y) mustBe true
    }

    "return false if cell x fully dominates cell y" in {
      val x = Cell(Seq(1, 1, 1))
      val y = Cell(Seq(2, 2, 2))

      x.partiallyDominates(y) mustBe false
    }

    "return false if cell x does not dominate cell y" in {
      val x = Cell(Seq(1, 2, 1))
      val y = Cell(Seq(2, 0, 2))

      x.partiallyDominates(y) mustBe false
    }
  }

  "mirrorCoordinates method" must {
    "return a Cell object with mirrored coordinates" in {
      val cell = Cell(Seq(0, 3, 2))
      val cellsPerDimension = 5

      cell.mirrorCoordinates(cellsPerDimension) mustBe Cell(Seq(4, 1, 2))
    }
  }

  "apply method" must {
    "convert a cell id to a Cell object" in {
      val cellId = 15L
      val dimensions = 3
      val cellsPerDimension = 3

      Cell(cellId, dimensions, cellsPerDimension) mustBe Cell(Seq(1, 2, 0))
    }
  }
}
