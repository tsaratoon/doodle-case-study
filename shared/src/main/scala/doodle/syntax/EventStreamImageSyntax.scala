package doodle.syntax

import doodle.event.EventStream
import doodle.event.Canvas
import doodle.backend.Canvas
import doodle.core.Image
import doodle.core.Color

trait EventStreamImageSyntax {
  
  implicit class EventStreamOps(val frames: EventStream[Image]) {
    
    def animate(implicit canvas: Canvas) =
      frames.map(frame => {
        canvas.clear(Color.black)
        frame.draw(canvas)
      })
               
  } 
  
}