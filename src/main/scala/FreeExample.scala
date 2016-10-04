import scalaz._
import Scalaz._

/**
  * Simplified from original post at https://www.chrisstucchio.com/blog/2015/free_monads_in_scalaz.html
  */
object FreeExample extends App {
  case class MyOp[A](action: String => A)

  implicit object MyOp extends Functor[MyOp] {
    def map[A, B](a: MyOp[A])(g: A => B) = MyOp[B]((s:String) => g(a.action(s)))
  }

  type Op[A] = Free[MyOp, A]

  val myOp: Op[Int] = Free.liftF(
    MyOp((s: String) => {
      println(s"Doing something with $s")
      s.length
    })
  )

  def run[A](s: String, op: Op[A]): A = op.fold((a: A) => a, (x: MyOp[Op[A]]) => run(s, x.action(s)))

  println(run("Howdy!", myOp))
}


