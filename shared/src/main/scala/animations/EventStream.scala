package animations

import scala.math
import doodle.backend.Key

sealed trait EventStream[A] {
  
  def map[B](f: A => B): EventStream[B] =
    Map(f)
    
  def join[B](that: EventStream[B]): EventStream[(A,B)] =
    Join(this, that)
  
  def scan[B](seed: B)(f: (A,B) => B): EventStream[B] =
    Scan(seed)(f)
    
  def createSource(input: List[Key]): EventStream[A] =
    Source(input)
  
}
import doodle.backend.Key
final case class Map[A, B](f: A => B) extends EventStream[B]
final case class Join[A, B](left: EventStream[A], right: EventStream[B]) extends EventStream[(A,B)]
final case class Scan[A, B](seed: B)(f: (A,B) => B) extends EventStream[B]
final case class Source[A](input: List[Key]) extends EventStream[A]
