package doodle.core.tsaratoon

import doodle.jvm.Java2DCanvas
import doodle.core.{Stroke,Color,Line,Normalized}
import scala.util.Random
import doodle.core.DrawingContext

object TestApp extends App {
  
  val rand = new Random
  
  val canvas = Java2DCanvas.canvas
  val context = DrawingContext.blackLines
  
  val width = 400
  val height = 400
  canvas.setSize(width, height)
  
//  def singleCircle(n: Int): Image = {
//    val opacity = Normalized(n / 20)
//    Circle(50 + 5 * n) lineColor (Color.red fadeOut opacity)
//  }
//
//  def concentricCircles(n: Int): Image =
//    if(n == 1) {
//      singleCircle(n)
//    } else {
//      singleCircle(n) on concentricCircles(n - 1)
//    }
//  
//  concentricCircles(20).draw(canvas, context)
  
  // London underground sign:
  
  val redCircle = Circle(80) lineWidth (25) lineColor (Color.red)
  val whiteBar = Rectangle(250,50) fillColor (Color.blue) lineWidth(5) lineColor(Color.blue)
  
  (whiteBar on redCircle).draw(canvas, context)
//  (whiteBar beside redCircle).draw(canvas, context)

}