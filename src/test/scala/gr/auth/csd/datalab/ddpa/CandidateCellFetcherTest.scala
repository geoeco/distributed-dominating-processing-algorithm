package gr.auth.csd.datalab.ddpa

import gr.auth.csd.datalab.ddpa.implicits.CellConverter
import gr.auth.csd.datalab.ddpa.schema.{CellAttributes, CellLowerBounds}
import org.scalatest.{FunSuite, Matchers}

class CandidateCellFetcherTest extends FunSuite with Matchers {

  import CandidateCellFetcherTest._

  private[this] val candidateCellFetcher = new CandidateCellFetcher(k)

  test("it should return the candidate cells with their lower bounds") {
    val cellAttributesPerCell = Map(
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
      Seq(3, 3).toCell -> CellAttributes(10, 0, 10, 80))

    val actual = candidateCellFetcher.fetch(cellAttributesPerCell)
    val expected = Map(
      Seq(0, 1).toCell -> CellLowerBounds(60, 0),
      Seq(0, 2).toCell -> CellLowerBounds(30, 0),
      Seq(1, 0).toCell -> CellLowerBounds(60, 0),
      Seq(1, 1).toCell -> CellLowerBounds(40, 0),
      Seq(2, 0).toCell -> CellLowerBounds(30, 0))

    actual shouldBe expected
  }
}

object CandidateCellFetcherTest {
  val k = 2
}
