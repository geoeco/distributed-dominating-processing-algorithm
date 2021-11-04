package gr.auth.csd.datalab.ddpa.config

final case class QueryConfig(
  k: Int,
  dimensions: Int,
  cellsPerDimension: Int,
  cellWidth: Double,
  minAllowedCoordinateValue: Double
)

object QueryConfig {

  def fromCommandLineConfig(commandLineConfig: CommandLineConfig): QueryConfig =
    QueryConfig(
      k = commandLineConfig.k,
      dimensions = commandLineConfig.dimensions,
      cellsPerDimension = commandLineConfig.cellsPerDimension,
      cellWidth = commandLineConfig.getCellWidth,
      minAllowedCoordinateValue = commandLineConfig.minAllowedCoordinateValue
    )
}
