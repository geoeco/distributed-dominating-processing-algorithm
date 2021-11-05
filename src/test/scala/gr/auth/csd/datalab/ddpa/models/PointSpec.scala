package gr.auth.csd.datalab.ddpa.models

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class PointSpec extends AnyWordSpec with Matchers {

  "dominates method" must {
    "return true if point x fully dominates point y" in {
      val x = Point(Seq(0.2, 0.3), Cell(Seq(0, 0)))
      val y = Point(Seq(0.6, 0.7), Cell(Seq(1, 1)))

      x.dominates(y) mustBe true
    }

    "return true if point x partially dominates point y " in {
      val x = Point(Seq(0.3, 0.7), Cell(Seq(0, 1)))
      val y = Point(Seq(0.6, 0.7), Cell(Seq(1, 1)))

      x.dominates(y) mustBe true
    }

    "return false if point x does not dominate point y" in {
      val x = Point(Seq(0.6, 0.7), Cell(Seq(1, 1)))
      val y = Point(Seq(0.2, 0.3), Cell(Seq(0, 0)))

      x.dominates(y) mustBe false
    }
  }

  "apply method" must {
    "parse a string to a Point object" in {
      val rawPoint = "0.6,0.3"
      val cellWidth = 0.5
      val minAllowedCoordinateValue = 0.0

      val actual = Point(rawPoint, cellWidth, minAllowedCoordinateValue)
      val expected = Point(Seq(0.6, 0.3), Cell(Seq(1, 0)))

      actual mustBe expected
    }
  }
}
