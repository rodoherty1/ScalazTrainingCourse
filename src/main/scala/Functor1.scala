


object Functor1 {

  import scala.language.higherKinds
  import scalaz.Functor
  import scalaz.syntax.functor._
  import scalaz.std.list._
  import scalaz.std.option._


  def add42[F[_]](f: F[Int])(implicit functor:Functor[F]): F[Int] = {
//    functor.map(f)(_ + 42)
    f.map(_ + 42)
  }


  // The TypeClass ... Functor[List[_]]
  // The type class instances  e.g. List(1, 2, 3)
  // The syntax


  val lists   = add42(List(1, 2, 3))

  val options = add42(Option(1))
}
