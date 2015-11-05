package animations

import scala.math
import doodle.backend.Key
import scala.collection.mutable

sealed trait EventStream[A] {
  
  val listeners: mutable.ListBuffer[EventStream[A]] =
    new mutable.ListBuffer()
  
  def map[B](f: A => B): EventStream[B] = {
    val node = Map(f)
    
    listeners += (node)
    
    node
  }
    
  def join[B](that: EventStream[B]): EventStream[(A,B)] =
    Join(this, that)
  
  def scan[B](seed: B)(f: (A,B) => B): EventStream[B] =
    Scan(seed)(f)
    
  def createSource(): EventStream[A] =
    Source()
    
  def transmit(in: A): Unit =
    listeners.foreach(_.push(in))
  
}

object EventStream {
  
  def fromCallbackHandler[A](handler: (A => Unit) => Unit) = {
    val stream = new Source[A]()
    handler((event: A) => stream.push(event))
    stream
  }
  
}



final case class Map[A, B](f: A => B) extends EventStream[B] {
  def push(in: A): Unit =
    transmit(f(in))
}
final case class Join[A, B](left: EventStream[A], right: EventStream[B]) extends EventStream[(A,B)]
final case class Scan[A, B](seed: B)(f: (A,B) => B) extends EventStream[B]
final case class Source[A]() extends EventStream[A]{
  def push(event: A): Unit = {
    transmit(event)
  }
}