package animations

import scala.math
import doodle.backend.Key
import scala.collection.mutable

sealed trait Listener[A]{
  
  def listen(in: A): Unit =
    this match {
      case m @ Map(f) => m.listeners.foreach(i => i.listen(f(in)))
//      case j @ Join(l,r) => j
      case s @ Scan(seed, f) => s.listeners.foreach(i => i.listen(f(in, seed)))
    }
  
}

sealed trait EventStream[A] {
  
  val listeners: mutable.ListBuffer[Listener[A]] =
    new mutable.ListBuffer()
  
  def map[B](f: A => B): EventStream[B] = {
    val node = Map(f)
    listeners += node
    node
  }
    
  def join[B](that: EventStream[B]): EventStream[(A,B)] = {
    val node = Join(this, that)
    this.listeners += node.left
    that.listeners += node.right
    node
  }
  
  def scan[B](seed: B)(f: (A,B) => B): EventStream[B] = {
    val node = Scan(seed,f)
    listeners += node
    node
  }
    
  def createSource(list: List[A]): EventStream[A] = {
    val source = Source[A]()
    list.foreach(i => source.push(i))
    source
  }
    
  def transmit(in: A): Unit =
    listeners.foreach(_.listen(in))
  
}

object EventStream {
  def fromCallbackHandler[A](handler: (A => Unit) => Unit) = {
    val node = new Source[A]()
    handler((event: A) => node.push(event))
    node
  }
}

final case class Map[A, B](f: A => B) extends Listener[A] with EventStream[B] {
  def push(in: A): Unit =
    transmit(f(in))
}

final case class Join[A, B](left: EventStream[A], right: EventStream[B]) extends Listener[(A,B)] with EventStream[(A,B)]

final case class Scan[A, B](seed: B, f: (A,B) => B) extends Listener[A] with EventStream[B]

final case class Source[A]() extends Listener[A] with EventStream[A] {
  def push(event: A): Unit = {
    transmit(event)
  }
}