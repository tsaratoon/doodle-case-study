package doodle.core.tsaratoon

object PathElement {
  
  sealed trait PathElement
  
  final case class MoveTo(x: Double, y: Double) extends PathElement
  final case class LineTo(x: Double, y: Double) extends PathElement
  final case class CurveTo(
      p0: Double,
      p1: Double,
      p2: Double,
      p3: Double,
      endX: Double,
      endY: Double
  ) extends PathElement
  
  final case class Path(elements: Seq[PathElement]) extends PathElement
  
  final case class Image(path: Path)
  
}