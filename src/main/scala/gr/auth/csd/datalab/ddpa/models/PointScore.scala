package gr.auth.csd.datalab.ddpa.models

final case class PointScore(point: Seq[Double], score: Long) {
  override def toString: String = s"${point.mkString(",")}\t$score"
}
