package doodle.core.tsaratoon.DoodleImages

import doodle.jvm.Java2DCanvas
import doodle.core.tsaratoon.Image.Circle
import doodle.core.tsaratoon.Image.Rectangle
import doodle.core.tsaratoon.Image.Beside


object TestApp extends App{
  
  val canvas = Java2DCanvas.canvas
  canvas.setSize(400, 400)
  
  val bottom = Circle(50)
  val top = Rectangle(30,60)
  
//  On(top,bottom).draw(canvas)
  Beside(top,bottom).draw(canvas)

}