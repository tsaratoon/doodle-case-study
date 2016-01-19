package doodle.event.tsaratoon

import doodle.backend.Key
import doodle.core._
import doodle.event._
import doodle.jvm.Java2DCanvas

object TestApp extends App {
  
  val canvas = Java2DCanvas.canvas
  canvas.setSize(400, 400)
  
  SmallExample.go(canvas)
  
}