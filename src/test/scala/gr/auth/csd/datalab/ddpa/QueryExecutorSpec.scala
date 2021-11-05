package gr.auth.csd.datalab.ddpa

import gr.auth.csd.datalab.ddpa.config.QueryConfig
import gr.auth.csd.datalab.ddpa.models.PointScore
import gr.auth.csd.datalab.ddpa.spark.SharedSparkSession
import org.scalatest.{Matchers, WordSpec}

class QueryExecutorSpec extends WordSpec with Matchers with SharedSparkSession {

  import QueryExecutorSpec._

  private val queryExecutor = QueryExecutor(queryConfig)(spark)

  "QueryExecutor" should {
    "return the k points with the highest score" in {
      val inputPath = getClass.getResource("/input.txt").getPath

      val actual = queryExecutor.execute(inputPath)
      val expected = Seq(
        PointScore(Seq(0.09702953194077146, 0.03178307828399585, 0.03820546540259484), 678),
        PointScore(Seq(0.0789317701492821, 0.09115389837640175, 0.03702042816947926), 657),
        PointScore(Seq(0.03314079856488339, 0.24814301589895615, 4.410912092605024e-4), 596),
        PointScore(Seq(0.08045132847522041, 0.10116638544152068, 0.13627011847840742), 590),
        PointScore(Seq(0.09413505690735313, 0.16749068281405377, 0.06636121487200253), 573),
        PointScore(Seq(0.09896261936027662, 0.18143469822052172, 0.032130782614466114), 572),
        PointScore(Seq(0.03770613260415734, 0.2945584029439181, 0.037912079495927675), 535),
        PointScore(Seq(0.30780534838295504, 0.03746794860404956, 0.06630889887025859), 512),
        PointScore(Seq(0.07765325189727956, 0.3053961830758003, 0.04107907569810354), 497),
        PointScore(Seq(0.1420590296524259, 0.24467124493142756, 0.07746961763021076), 492)
      )

      actual shouldBe expected
    }
  }
}

object QueryExecutorSpec {
  val queryConfig = QueryConfig(10, 3, 5, 0.2, 0.0)
}
