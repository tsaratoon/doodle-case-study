package doodle.core.tsaratoon

import doodle.backend.Canvas

object Image {
  sealed trait Image {
    def on(that: Image): Image =
      On(this, that)
      
    def above(that: Image): Image =
      Above(this, that)
  
    def beside(that: Image): Image =
      Beside(this, that)
      
    def stroke(width: Double): Image = 
      Stroke(width)
  
    def draw(canvas: Canvas): Unit =
      ??? // structural recursion
  
    // A helper method you will probably want
    def draw(canvas: Canvas, originX: Double, originY: Double): Unit =
      ???

    // Need bounding box
  }
  
  final case class Circle(radius: Double) extends Image
  final case class Rectangle(width: Double, height: Double) extends Image
  final case class Above(above: Image, below: Image) extends Image
  final case class Beside(left: Image, right: Image) extends Image
  final case class On(top: Image, bottom: Image) extends Image
  final case class Stroke(width: Double) extends Image
  
}