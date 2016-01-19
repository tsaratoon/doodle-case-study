package doodle.core.tsaratoon

import doodle.backend.Canvas
import doodle.core.Vec
import doodle.core.DrawingContext
import doodle.core.Color

sealed trait Image {
  
	val boundingBox: BoundingBox = {
		this match {
		  case Circle(r) =>
		    BoundingBox(-r, r, r, -r)
		  case Rectangle(w,h) =>
		    BoundingBox(-w/2, h/2, w/2, -h/2)
		  case On(o,u) =>
		    o.boundingBox on u.boundingBox
		  case Beside(l,r) =>
		    l.boundingBox beside r.boundingBox
		  case Above(a,b) =>
		    a.boundingBox above b.boundingBox
		  case At(v,i) =>
		    i.boundingBox translate v
		  case ContextTransform(f,i) =>
		    i.boundingBox
		}
  }
	
  def on(that: Image): Image =
    On(this, that)
    
  def beside(that: Image): Image =
    Beside(this, that)
    
  def above(that: Image): Image =
    Above(this, that)
    
  def at(x: Double, y: Double): Image =
    At(Vec(x, y), this)
    
  def lineColor(color: Color): Image =
    ContextTransform(_.lineColor(color), this)

  def lineWidth(width: Double): Image =
    ContextTransform(_.lineWidth(width), this)

  def fillColor(color: Color): Image =
    ContextTransform(_.fillColor(color), this) 
    
  def draw(canvas: Canvas, context: DrawingContext): Unit =
    draw(canvas,context, 0, 0)
    
  def draw(canvas: Canvas, context: DrawingContext, originX: Double, originY: Double): Unit = {
    
    def doStrokeAndFill() = {
      context.fill.foreach { fill =>
        canvas.setFill(fill.color)
        canvas.fill()
      }

      context.stroke.foreach { stroke =>
        canvas.setStroke(stroke)
        canvas.stroke()
      }
    }
    
    this match {
      case Circle(r) =>
        canvas.circle(originX, originY, r)
        doStrokeAndFill()
      case Rectangle(w,h) =>
        canvas.rectangle(originX - w/2, originY + h/2, w, h)
        doStrokeAndFill()
      case On(o,u) =>
        u.draw(canvas, context, originX, originY)
        o.draw(canvas, context, originX, originY)
      case Beside(l,r) =>
        val box = this.boundingBox
        val lBox = l.boundingBox
        val rBox = r.boundingBox
        
        val leftOriginX =
          originX + box.left + (lBox.width / 2)
        val rightOriginX =
          originX + box.right - (rBox.width / 2)
        
        l.draw(canvas, context, leftOriginX, originY)
        r.draw(canvas, context, rightOriginX, originY)
      case Above(a,b) => 
        val box = this.boundingBox
        val aBox = a.boundingBox
        val bBox = b.boundingBox
        
        val aboveOriginY = 
          originY + box.top - (aBox.height / 2)
        val belowOriginY =
          originY + box.bottom + (bBox.height / 2)
          
        a.draw(canvas, context, originX, aboveOriginY)
        b.draw(canvas, context, originX, belowOriginY)
      case At(v,i) =>
        i.draw(canvas, context, originX + v.x, originY + v.y)
      case ContextTransform(f, i) =>
        i.draw(canvas, f(context), originX, originY)
    }
    
  }
  
}
final case class Circle(radius: Double) extends Image
final case class Rectangle(width: Double, height: Double) extends Image
final case class On(on: Image, under: Image) extends Image
final case class Beside(left: Image, right: Image) extends Image
final case class Above(above: Image, below: Image) extends Image
final case class At(at: Vec, i: Image) extends Image
final case class ContextTransform(f: DrawingContext => DrawingContext, image: Image) extends Image