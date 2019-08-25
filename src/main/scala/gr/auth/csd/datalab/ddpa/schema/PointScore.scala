package gr.auth.csd.datalab.ddpa.schema

case class PointScore(point: Seq[Double], score: Long) {
  override def toString: String = s"${point.mkString(",")}\t$score"
}