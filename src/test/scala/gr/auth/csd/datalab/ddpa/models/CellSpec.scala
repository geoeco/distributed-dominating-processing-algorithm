package gr.auth.csd.datalab.ddpa.models

import org.scalatest.{Matchers, WordSpec}

class CellSpec extends WordSpec with Matchers {

  "partiallyDominates method" should {
    "return true if cell x partially dominates cell y" in {
      val x = Cell(Seq(1, 2, 1))
      val y = Cell(Seq(2, 2, 2))

      x.partiallyDominates(y) shouldBe true
    }

    "return false if cell x fully dominates cell y" in {
      val x = Cell(Seq(1, 1, 1))
      val y = Cell(Seq(2, 2, 2))

      x.partiallyDominates(y) shouldBe false
    }

    "return false if cell x does not dominate cell y" in {
      val x = Cell(Seq(1, 2, 1))
      val y = Cell(Seq(2, 0, 2))

      x.partiallyDominates(y) shouldBe false
    }
  }

  "mirrorCoordinates method" should {
    "return a Cell object with mirrored coordinates" in {
      val cell = Cell(Seq(0, 3, 2))
      val cellsPerDimension = 5

      cell.mirrorCoordinates(cellsPerDimension) shouldBe Cell(Seq(4, 1, 2))
    }
  }

  "apply method" should {
    "convert a cell id to a Cell object" in {
      val cellId = 15L
      val dimensions = 3
      val cellsPerDimension = 3

      Cell(cellId, dimensions, cellsPerDimension) shouldBe Cell(Seq(1, 2, 0))
    }
  }
}
