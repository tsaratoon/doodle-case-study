package doodle.core.week6

import doodle.jvm.Java2DCanvas
import doodle.core._
import doodle.examples.Ufo
import typeclasses.FunctorSyntax
import typeclasses.Functor
import scala.language.higherKinds

object TestApp extends App {
 
//  implicit val canvas = Java2DCanvas.canvas
//  canvas.setSize(600, 600)
 
  // Check draw can use implicit canvas:
//  val bottom = Circle(50)
//  val top = Rectangle(30,60)
//  
//  Beside(top,bottom).draw
  
  // Check EventStreamImageSyntax is working:
//  Ufo.go(canvas)
  
  // Example for Functor:
  
//  sealed trait Result[A] {
//  def map[B](f: A => B): Result[B] =
//    this match {
//      case Full(a) => Full(f(a))
//      case Empty() => Empty()
//    }
//  }
//  object Result {
//    implicit object resultInstances extends Functor[Result] {
//      def map[A,B](in: Result[A])(f: A => B): Result[B] =
//        in.map(f)
//    }
//  
//    def full[A](in: A): Result[A] =
//      Full(in)
//  }
//  final case class Full[A](get: A) extends Result[A]
//  final case class Empty[A]() extends Result[A]
//  
//  object Example {
//    import FunctorSyntax._
//  
//    def transform[F[_] : Functor](in: F[Int]) =
//      in.map(x => x + 42)
//  
//    def go = {
//      println(transform(List(1,2,3)))
//      println(transform(Result.full(1)))
//    }
//  
//  }
  
  object Example {
    
    import FunctorSyntax._
    
    def go = {
      ???
    }    
    
  }
  
  Example.go
  
  

}