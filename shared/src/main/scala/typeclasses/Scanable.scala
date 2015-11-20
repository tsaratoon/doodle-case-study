package typeclasses

import scala.language.higherKinds

trait Scanable[F[_]] {
  def scanLeft[A, B](seed: B)(f: (A, B) => B): F[B]
}
object Scanable {
  implicit object listInstances extends Scanable[List] {
    def scanLeft[A, B](fa: List[A])(b: B)(f: (B, A) => B): List[B] =
      fa.scanLeft(b)(f)
  }
}