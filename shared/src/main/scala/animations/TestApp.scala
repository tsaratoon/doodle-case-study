package animations

import doodle.jvm.Java2DCanvas
import doodle.backend.Key
import doodle.core.Circle
import doodle.core.Color
import doodle.core.Vec

object TestApp extends App {
  
  // Simple test
//  val source = Source[Int]()
//  List(1,2,3,4).foreach(source.push)
//  println(source)
  
  val source = Source[Int]().createSource(List(1,2,3,4))
  println(source.listeners)
  
  // Simple test for map
//  val timesTen = source.map( i => println(i) )
//  source.join(timesTen).map{case (l,r) => println(l+r)}
  
//  val canvas = Java2DCanvas.canvas
//  
//  canvas.setSize(600, 600)
//  
//  val redraw: EventStream[Double] = Canvas.animationFrameEventStream(canvas)
//  val keys: EventStream[Key] = Canvas.keyDownEventStream(canvas)
//
//  val ball = Circle(20) fillColor (Color.red) lineColor (Color.red)
//  
//  val velocity: EventStream[Vec] =
//    keys.scan(Vec.zero)((key, prev) => {
//        val velocity =
//          key match {
//            case Key.Up    => prev + Vec(0, 1)
//            case Key.Right => prev + Vec(1, 0)
//            case Key.Down  => prev + Vec(0, -1)
//            case Key.Left  => prev + Vec(-1, 0)
//            case _         => prev
//          }
//        Vec(velocity.x.min(5).max(-5), velocity.y.min(5).max(-5))
//      }
//    )
    
  
  
  
}