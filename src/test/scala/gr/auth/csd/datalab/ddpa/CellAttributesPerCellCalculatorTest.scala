package gr.auth.csd.datalab.ddpa

import gr.auth.csd.datalab.ddpa.implicits.CellConverter
import gr.auth.csd.datalab.ddpa.models.{Cell, CellAttributes}
import org.scalatest.{FunSuite, Matchers}

class CellAttributesPerCellCalculatorTest extends FunSuite with Matchers {

  import CellAttributesPerCellCalculatorTest._

  private val cellAttributesPerCellCalculator =
    new CellAttributesPerCellCalculator(dimensions, cellsPerDimension)

  test(
    "it should calculate the cell attributes for all cells containing " +
      "points") {
    val pointCountsPerCell: Map[Cell, Long] = Map(
      Seq(0, 1).toCell -> 10,
      Seq(0, 2).toCell -> 10,
      Seq(0, 3).toCell -> 10,
      Seq(1, 0).toCell -> 10,
      Seq(1, 1).toCell -> 10,
      Seq(1, 2).toCell -> 10,
      Seq(1, 3).toCell -> 10,
      Seq(2, 0).toCell -> 10,
      Seq(2, 1).toCell -> 10,
      Seq(2, 2).toCell -> 10,
      Seq(2, 3).toCell -> 10,
      Seq(3, 0).toCell -> 10,
      Seq(3, 1).toCell -> 10,
      Seq(3, 2).toCell -> 10,
      Seq(3, 3).toCell -> 10
    )

    val actual = cellAttributesPerCellCalculator.calculate(pointCountsPerCell)
    val expected = Map(
      Seq(0, 1).toCell -> CellAttributes(10, 60, 120, 0),
      Seq(0, 2).toCell -> CellAttributes(10, 30, 80, 0),
      Seq(0, 3).toCell -> CellAttributes(10, 0, 40, 0),
      Seq(1, 0).toCell -> CellAttributes(10, 60, 120, 0),
      Seq(1, 1).toCell -> CellAttributes(10, 40, 90, 0),
      Seq(1, 2).toCell -> CellAttributes(10, 20, 60, 10),
      Seq(1, 3).toCell -> CellAttributes(10, 0, 30, 20),
      Seq(2, 0).toCell -> CellAttributes(10, 30, 80, 0),
      Seq(2, 1).toCell -> CellAttributes(10, 20, 60, 10),
      Seq(2, 2).toCell -> CellAttributes(10, 10, 40, 30),
      Seq(2, 3).toCell -> CellAttributes(10, 0, 20, 50),
      Seq(3, 0).toCell -> CellAttributes(10, 0, 40, 0),
      Seq(3, 1).toCell -> CellAttributes(10, 0, 30, 20),
      Seq(3, 2).toCell -> CellAttributes(10, 0, 20, 50),
      Seq(3, 3).toCell -> CellAttributes(10, 0, 10, 80)
    )

    actual shouldBe expected
  }
}

object CellAttributesPerCellCalculatorTest {
  val dimensions = 2
  val cellsPerDimension = 4
}
