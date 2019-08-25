package gr.auth.csd.datalab.ddpa.config

case class QueryConfig(
  k: Int,
  dimensions: Int,
  cellsPerDimension: Int,
  cellWidth: Double,
  minAllowedCoordinateValue: Double
)
