package doodle.core.tsaratoon

import doodle.jvm.Java2DCanvas
import doodle.core._
import doodle.core.tsaratoon.Image._

object TestApp extends App{
  
  val canvas = Java2DCanvas.canvas
  canvas.setSize(400, 400)
  
  Circle(50).draw(canvas)
  
  Rectangle(30,60).draw(canvas)

}