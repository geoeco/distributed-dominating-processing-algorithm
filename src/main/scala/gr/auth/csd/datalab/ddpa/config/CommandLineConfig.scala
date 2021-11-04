package gr.auth.csd.datalab.ddpa.config

final case class CommandLineConfig(
  k: Int = 0,
  dimensions: Int = 0,
  cellsPerDimension: Int = 0,
  minAllowedCoordinateValue: Double = 0.0, // inclusive
  maxAllowedCoordinateValue: Double = 1.0, // exclusive
  inputPath: String = "",
  outputDir: String = ""
) {
  def getCellWidth: Double =
    (maxAllowedCoordinateValue - minAllowedCoordinateValue) / cellsPerDimension
}

object CommandLineConfig {

  def parse(args: Array[String]): Option[CommandLineConfig] = {
    val parser = new scopt.OptionParser[CommandLineConfig]("DDPA") {
      opt[Int]('k', "k")
        .required
        .validate { x =>
          if (x > 0) success
          else failure("k must be higher than zero")
        }
        .action((x, c) => c.copy(k = x))
        .text("number of results of the top-k dominating query")

      opt[Int]('d', "dimensions")
        .required
        .validate { x =>
          if (x > 0) success
          else failure("dimensions must be higher than zero")
        }
        .action((x, c) => c.copy(dimensions = x))
        .text("number of dimensions")

      opt[Int]('m', "cellsPerDimension")
        .required
        .validate { x =>
          if (x > 0) success
          else failure("number of cells must be higher than zero")
        }
        .action((x, c) => c.copy(cellsPerDimension = x))
        .text("number of grid cells per dimension")

      opt[Double]("min")
        .action((x, c) => c.copy(minAllowedCoordinateValue = x))
        .text("minimum allowed value for a coordinate (inclusive)")

      opt[Double]("max")
        .action((x, c) => c.copy(maxAllowedCoordinateValue = x))
        .text("maximum allowed value for a coordinate (exclusive)")

      opt[String]('i', "inputPath")
        .required
        .action((x, c) => c.copy(inputPath = x))
        .text("path to input file(s)")

      opt[String]('o', "outputDir")
        .required
        .action((x, c) => c.copy(outputDir = x))
        .text("path to output directory (must exist)")

      checkConfig { c =>
        if (c.minAllowedCoordinateValue < c.maxAllowedCoordinateValue)
          success
        else
          failure("minimum value should be lower than maximum value")
      }
    }
    parser.parse(args, CommandLineConfig())
  }
}
