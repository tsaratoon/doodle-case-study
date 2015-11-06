package animations

import scala.math
import doodle.backend.Key
import scala.collection.mutable

sealed trait Listener[A]{
  
  def listen(in: A): Unit =
    this match {
      case m @ Map(f) => m.transmit(f(in))
//      case j @ Join(l,r) => 
      case s @ Scan(acc, f) => {
        val result = f(in, acc)
        s.acc = result
        s.transmit(result)
      }
      case s @ Source() => s.transmit(in)
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
    this.listeners += node.leftListener
    that.listeners += node.rightListener
    node
  }
  
  def scan[B](acc: B)(f: (A,B) => B): EventStream[B] = {
    val node = Scan(acc,f)
    listeners += node
    node
  }
    
//  def createSource(list: List[A]): EventStream[A] = {
//    val source = Source[A]()
//    list.foreach(i => source.push(i))
//    source
//  }
    
  def transmit(in: A): Unit =
    listeners.foreach(_.listen(in))
  
}

object EventStream {
  def fromCallbackHandler[A](handler: (A => Unit) => Unit) = {
    val node = new Source[A]()
    handler((event: A) => node.listen(event))
    node
  }
}

final case class Map[A, B](f: A => B) extends Listener[A] with EventStream[B]

final case class Join[A, B]() extends EventStream[(A,B)] {
  var leftValue: Option[A] = None
  var rightValue: Option[B] = None
  
  val leftListener: Listener[A] = {
    new Listener[A]{
      override def listen(in: A): Unit {
        leftValue = Some(in)
        rightValue.foreach(r => transmit( (in,r) ))
      }
    }
  }
  
  val rightListener: Listener[B] = {
    new Listener[B]{
      override def listen(in: B): Unit {
        rightValue = Some(in)
        leftValue.foreach(l => transmit( (in,l) ))
      }
    }
  }
  
}

final case class Scan[A, B](var acc: B, f: (A,B) => B) extends Listener[A] with EventStream[B]

final case class Source[A]() extends Listener[A] with EventStream[A]

object Example {
  def go = {
  var results: List[Int] = List.empty[Int]
  val source: Source[Int] = Source()
//  source.map(x => results = x +: results)
  source.scan(0){ (elt,accum) =>
    results = (elt + accum) +: results
    elt + accum
  }

  List(1,2,3).foreach(source.listen(_))
  println(results)
  }
}