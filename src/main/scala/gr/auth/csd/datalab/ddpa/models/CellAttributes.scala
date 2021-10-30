package gr.auth.csd.datalab.ddpa.models

final case class CellAttributes(
    pointCount: Long,
    lowerDominatingBound: Long,
    upperDominatingBound: Long,
    lowerDominatedBound: Long
)
