package gr.auth.csd.datalab.ddpa.schema

import org.scalatest.{FunSuite, Matchers}

class PointTest extends FunSuite with Matchers {

  test("it should return true if point x fully dominates point y") {
    val x = Point(Seq(0.2, 0.3), Cell(Seq(0, 0)))
    val y = Point(Seq(0.6, 0.7), Cell(Seq(1, 1)))

    x.dominates(y) shouldBe true
  }

  test("it should return true if point x partially dominates point y ") {
    val x = Point(Seq(0.3, 0.7), Cell(Seq(0, 1)))
    val y = Point(Seq(0.6, 0.7), Cell(Seq(1, 1)))

    x.dominates(y) shouldBe true
  }

  test("it should return false if point x does not dominate point y") {
    val x = Point(Seq(0.6, 0.7), Cell(Seq(1, 1)))
    val y = Point(Seq(0.2, 0.3), Cell(Seq(0, 0)))

    x.dominates(y) shouldBe false
  }

  test("it should parse a string to a Point object") {
    val rawPoint = "0.6,0.3"
    val cellWidth = 0.5
    val minAllowedCoordinateValue = 0.0

    val actual = Point(rawPoint, cellWidth, minAllowedCoordinateValue)
    val expected = Point(Seq(0.6, 0.3), Cell(Seq(1, 0)))

    actual shouldBe expected
  }
}
