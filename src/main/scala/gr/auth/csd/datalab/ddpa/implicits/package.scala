package gr.auth.csd.datalab.ddpa

import gr.auth.csd.datalab.ddpa.models.Cell

package object implicits {

  implicit class CellConverter(coordinates: Seq[Int]) {
    def toCell: Cell = Cell(coordinates)
  }
}
