package typeclasses

import scala.language.higherKinds

trait Functor[F[_]] {  
  def map[A,B](in: F[A])(f: A => B): F[B]  
}
object Functor {  
  implicit object listInstances extends Functor[List] {    
    def map[A, B](in: List[A])(f: A => B): List[B] =
      in.map(f)
    def flatMap[A, B](fa: List[A])(f: A => List[B]): List[B] =
      fa.flatMap(f)
    def point[A](a: A): List[A] =
      List(a)
    def zip[A, B](fa: List[A])(fb: List[B]): List[(A, B)] =
      fa.zip(fb)
  }  
}

object FunctorSyntax {  
  implicit class ToFunctorOps[F[_],A](in: F[A]) {    
    def map[B](f: A => B)(implicit functor: Functor[F]): F[B] =
      functor.map(in)(f)      
  }
}

trait Applicative[F[_]] extends Functor[F] {
  def zip[A, B](fa: F[A], fb: F[B]): F[(A, B)]
  def point[A](a: A): F[A]
}

trait Monad[F[_]] extends Functor[F] {
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
}
  
