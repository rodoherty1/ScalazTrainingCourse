import scalaz.Functor


object Functor1 {

  import scala.language.higherKinds
  import scalaz.Functor
  import scalaz.syntax.functor._
  import scalaz.std.list._
  import scalaz.std.option._


  def add42[F[_]](f: F[Int])(implicit functor: Functor[F]): F[Int] = {
    //    functor.map(f)(_ + 42)
    f.map(_ + 42)
  }


  // The TypeClass ... Functor[List[_]]
  // The type class instances  e.g. List(1, 2, 3)
  // The syntax


  val lists = add42(List(1, 2, 3))

  val options = add42(Option(1))
}


object MyFunctorDemo extends App {
  trait MyContainer[A] {
    def value: A
  }

  implicit val myFunctor: Functor[MyContainer]  = new Functor[MyContainer] {
    override def map[A, B](fa: MyContainer[A])(f: (A) => B): MyContainer[B] = {
      new MyContainer[B] {
        override def value: B = f(fa.value)
      }
    }
  }

  val myContainer = new MyContainer[String] {
    override def value: String = "Hello"
  }

  val reverseString: String => String = s => s.reverse

  def containerOp[A](myc: MyContainer[A])(f: A => A)(implicit fa: Functor[MyContainer]): MyContainer[A] = {
    fa.map(myc)(f)
  }

  println(containerOp(myContainer)(reverseString).value)
}





