package doodle.core.tsaratoon

import doodle.backend.Canvas
import doodle.core.Color
import doodle.core.Line.Cap
import doodle.core.Line.Join
import doodle.core.Stroke

object Image {
  
  sealed trait Image {
    def on(that: Image): Image =
      On(this, that)
      
    def above(that: Image): Image =
      Above(this, that)
  
    def beside(that: Image): Image =
      Beside(this, that)
      
    def stroke(width: Double, color: Color, cap: Cap, join: Join): Image = 
      ???
      //Stroke(width, color, cap, join)
      
    def fill(color: Color): Image = 
      ???
  
    def draw(canvas: Canvas): Unit = {
      val centreX = 0
      val centreY = 0
      canvas.setOrigin(centreX, centreY)
      this match {
        case Circle(radius) => {
          val c = 0.551915024494
          val cR = c * radius
          canvas.beginPath()
          canvas.moveTo(centreX, centreY + radius)
          canvas.bezierCurveTo(centreX + cR, centreY + radius,
            centreX + radius, centreY + cR,
            centreX + radius, centreY)
          canvas.bezierCurveTo(centreX + radius, centreY - cR,
            centreX + cR, centreY - radius,
            centreX, centreY - radius)
          canvas.bezierCurveTo(centreX - cR, centreY - radius,
            centreX - radius, centreY - cR,
            centreX - radius, centreY)
          canvas.bezierCurveTo(centreX - radius, centreY + cR,
            centreX - cR, centreY + radius,
            centreX, centreY + radius)
          canvas.endPath()

        }
        case Rectangle(width, height) => {
          val centerX = 0.0
          val centerY = 0.0
          canvas.beginPath()
          canvas.moveTo(centerX - width/2, centerY + height/2)
          canvas.lineTo(centerX - width/2, centerY - height/2)
          canvas.lineTo(centerX + width/2, centerY - height/2)
          canvas.lineTo(centerX + width/2, centerY + height/2)
          canvas.lineTo(centerX - width/2, centerY + height/2)
          canvas.endPath()
        }
        case Above(above,below) => {
          ???
        }
        case Beside(left, right) => {
          ???
        }
        case On(top, botom) => {
          ???
        }
      }
    }
  
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
  
}