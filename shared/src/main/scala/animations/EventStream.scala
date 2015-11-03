package animations

import scala.math
import doodle.backend.Key
import scala.collection.mutable

sealed trait EventStream[A] {
  
  val events: mutable.ListBuffer[EventStream[A]] =
    new mutable.ListBuffer()
  
  def map[B](f: A => B): EventStream[B] =
    Map(f)
    
  def join[B](that: EventStream[B]): EventStream[(A,B)] =
    Join(this, that)
  
  def scan[B](seed: B)(f: (A,B) => B): EventStream[B] =
    Scan(seed)(f)
    
  def createSource(): EventStream[A] =
    Source()
    
  def update(event: A): Unit =
    ???
  
}

object EventStream {
  
  def fromCallbackHandler[A](handler: (A => Unit) => Unit) = {
    val stream = new Source[A]()
    handler((event: A) => stream.push(event))
    stream
  }
  
}



final case class Map[A, B](f: A => B) extends EventStream[B]
final case class Join[A, B](left: EventStream[A], right: EventStream[B]) extends EventStream[(A,B)]
final case class Scan[A, B](seed: B)(f: (A,B) => B) extends EventStream[B]
final case class Source[A]() extends EventStream[A]{
  def push(event: A): Unit = {
    update(event)
  }
}