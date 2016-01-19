package doodle.event.tsaratoon

import doodle.backend.Key
import doodle.core._
import doodle.event._
import doodle.jvm.Java2DCanvas

object SmallExample {
  
  def go(implicit canvas: doodle.backend.Canvas): Unit = {
    
    val redraw = Canvas.animationFrameEventStream(canvas)
    val keys = Canvas.keyDownEventStream(canvas)
    
    val velocity = keys.scanLeft(Vec.zero)( (key,prev) => {
      val velocity =
        key match {
          case Key.Up    => prev + Vec(0,1)
          case Key.Down  => prev + Vec(0,-1)
          case Key.Right => prev + Vec(1,0)
          case Key.Left  => prev + Vec(-1,0)
          case _         => prev
        }
      Vec(velocity.x.min(5).max(-5), velocity.y.min(5).max(-5))
    })
    
//    // Updates position on key presses:
//    val location = velocity.foldp(Vec.zero){ (velocity, prev) =>
//      val location = prev + velocity
//      Vec(location.x.min(300).max(-300), location.y.min(300).max(-300))
//    }
    
    
    val location = redraw.join(velocity).map{ case(ts, m) => m }
      .scanLeft(Vec.zero){ (velocity, prev) =>
        val location = prev + velocity
        Vec(location.x.min(300).max(-300), location.y.min(300).max(-300))
      }
    
    val ball = Circle(20) fillColor (Color.red) lineColor (Color.green)
    
    val frames = location.map(location => ball at location)
    
    Canvas.animate(canvas, frames)
    
  }
  
}