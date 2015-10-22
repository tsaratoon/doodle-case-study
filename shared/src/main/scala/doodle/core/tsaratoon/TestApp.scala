package doodle.core.tsaratoon

import doodle.jvm.Java2DCanvas
import doodle.core._
import doodle.core.tsaratoon.Image._

object TestApp extends App{
  
  val canvas = Java2DCanvas.canvas
  canvas.setSize(400, 400)
  
  val bottom = Circle(50)
  val top = Rectangle(30,60)
  
//  On(top,bottom).draw(canvas)
  Beside(top,bottom).draw(canvas)

}