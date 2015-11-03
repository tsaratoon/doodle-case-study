package animations

import doodle.backend.Key
import doodle.core._
import doodle.backend
import doodle.jvm.Java2DCanvas

object Example extends App {
  
  val canvas = Java2DCanvas.canvas
  
  canvas.setSize(600, 600)
  canvas.setOrigin(0, 0)
  
  val initialLocation = Vec(0,0)
  
  // A simple image of a ball
  val ball = Circle(10) fillColor Color.red
  
  def currentBall(currentLocation: Vec): Image = 
    ball at currentLocation
    
  val input = List(Up, Down, Up, Down, Left, Right)
  
  
  sealed trait KeyPress
  final case class Up() extends KeyPress
  final case class Down() extends KeyPress
  final case class Left() extends KeyPress
  final case class Right() extends KeyPress
  
  
  def currentVelocity(input: KeyPress, previousVelocity: Vec): Vec =
    input match {
    case Up() => previousVelocity + Vec(0, 1)
    case Down() => previousVelocity + Vec(0, -1)
    case Left() => previousVelocity + Vec(-1, 0)
    case Right() => previousVelocity + Vec(1, 0)
    }
  
  // Location is represented as a two dimensional Vector, by abuse of notation
  def currentLocation(velocity: Vec, previousLocation: Vec): Vec =
    previousLocation + velocity
    
  
  
}