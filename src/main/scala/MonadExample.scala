import scalaz.Id

object MonadExample {
  import scala.language.higherKinds
  import scalaz.Monad
  import scalaz.syntax.monad._
  import scalaz.std.list._
  import scalaz.std.option._


  def add42[F[_] : Monad](f: F[Int]): F[Int] = {
    f.flatMap(x => (x + 42).point[F])
  }

  val list = add42(List(1, 2, 3))

  val option = add42(Option(1))

  val int = add42(1 : Id.Id[Int])


  def flatMap[X, A, B](fa: X => A)(f: A => (X => B)): (X => B) = {

    x => {
      val a: A = fa(x)
      val xToB: X => B = f(a)
      xToB(x)
    }

  }


  def pure[X, A](a: A): X => A = {
    x => a
  }
}
