package doodle
package event

sealed trait Observer[A] {
	
	def observe(in: A): Unit =
	  this match {
	    case m @ Map(f) =>
	      val output = f(in)
	      m.observers.foreach(o => o.observe(output))
	    case s @ ScanLeft(seed, f) =>
	      val output = f(in)
	      s.seed = output
	      s.observers.foreach(o => o.observe(output))
	  }
					
}

sealed trait EventStream[A] {
	import scala.collection.mutable
	
	val observers: mutable.ListBuffer[Observer[A]] =
	  new mutable.ListBuffer()
  
  def map[B](f: A => B): EventStream[B] = {
	  val node = Map(f)
	  observers += node
	  node
	}
  
  def scanLeft[B](seed: B)(f: A => B): EventStream[B] = {
    val node = ScanLeft(seed, f)
    observers += node
    node
  }
  
  def join[B](that: EventStream[B]): EventStream[(A,B)]
  
}

final case class Map[A,B](f: A => B) extends Observer[A] with EventStream[B]
final case class ScanLeft[A,B](seed: B, f: A => B) extends Observer[A] with EventStream[B]