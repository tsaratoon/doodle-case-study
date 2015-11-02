package doodle.core.tsaratoon

import doodle.core._
import doodle.backend._


object Image {
  
  sealed trait Image {
    
    def on(that: Image): Image =
      On(this, that)
      
    def above(that: Image): Image =
      Above(this, that)
  
    def beside(that: Image): Image =
      Beside(this, that)
  
    val defaultStroke = Stroke(3.0, Color.black, Line.Cap.Round, Line.Join.Round)
    val defaultFill = Color.black
    
    def boundingBox: BoundingBox = {
      this match {
        case Circle(radius) =>
          BoundingBox(-radius,radius,radius,-radius)
        case Rectangle(width, height) =>
          BoundingBox(-width/2,width/2,-height/2,height/2)
        case Above(above,below) => 
          BoundingBox(
              above.boundingBox.left min below.boundingBox.left,
              above.boundingBox.right min below.boundingBox.right,
              above.boundingBox.top,
              below.boundingBox.bottom)
        case Beside(first,second) => 
          BoundingBox(
              first.boundingBox.left,
              second.boundingBox.right,
              first.boundingBox.top min second.boundingBox.top,
              first.boundingBox.bottom min second.boundingBox.bottom)
        case On(under,over) => {
          BoundingBox(
              under.boundingBox.left min over.boundingBox.left,
              under.boundingBox.right min over.boundingBox.right,
              under.boundingBox.top min over.boundingBox.top,
              under.boundingBox.bottom min over.boundingBox.bottom)
        }
      }
    }
    
    def draw(canvas: Canvas, stroke: Stroke = defaultStroke): Unit = {
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
          canvas.setStroke(stroke)
          canvas.stroke()
          //TODO: Fill with colour

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
          canvas.setStroke(stroke)
          canvas.stroke()
          // TODO: Fill with colour
        }
        case Above(above,below) => {
          ???
          // TODO: Write method for placing shape above
        }
        case Beside(left, right) => {
          ???
          // TODO: Write method for placing shape beside
        }
        case On(top, bottom) => {
          bottom.draw(canvas)
          top.draw(canvas)
          // TODO: Write method for placing shape on top
        }
      }
    }
  
    // A helper method you will probably want
    def draw(canvas: Canvas, originX: Double, originY: Double): Unit =
      ???
    

  }
  
  final case class Circle(radius: Double) extends Image
  final case class Rectangle(width: Double, height: Double) extends Image
  final case class Above(above: Image, below: Image) extends Image
  final case class Beside(left: Image, right: Image) extends Image
  final case class On(top: Image, bottom: Image) extends Image
  
}