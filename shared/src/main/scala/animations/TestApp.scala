package animations

import doodle.jvm.Java2DCanvas
import doodle.backend.Key
import doodle.core.Circle
import doodle.core.Color
import doodle.core.Vec

object TestApp extends App {
  
  val canvas = Java2DCanvas.canvas
  
  canvas.setSize(600, 600)
  
  val redraw: EventStream[Double] = Canvas.animationFrameEventStream(canvas)
  val keys: EventStream[Key] = Canvas.keyDownEventStream(canvas)

  val ball = Circle(20) fillColor (Color.red) lineColor (Color.red)
  
  val velocity: EventStream[Vec] =
    keys.scan(Vec.zero)((key, prev) => {
        val velocity =
          key match {
            case Key.Up    => prev + Vec(0, 1)
            case Key.Right => prev + Vec(1, 0)
            case Key.Down  => prev + Vec(0, -1)
            case Key.Left  => prev + Vec(-1, 0)
            case _         => prev
          }
        Vec(velocity.x.min(5).max(-5), velocity.y.min(5).max(-5))
      }
    )
    
  
  
  
}