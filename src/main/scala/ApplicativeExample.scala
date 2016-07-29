import scala.concurrent.Future


object ApplicativeExample {

  import scalaz.syntax.applicative._
  import scalaz.std.list._
  import scalaz.std.scalaFuture._
  import scala.concurrent.ExecutionContext.Implicits.global

  val list = (List (1, 2, 3) |@| List (4, 5, 6)).tupled

  import scalaz.Validation
  import scalaz.std.list._
  import scalaz.syntax.validation._


  val username = List("No user exists").failure
  val password = List("password incorrect").failure
  val result = (username |@| password).tupled


  val notInParallel = for {
    a <- Future ("a")
    b <- Future ("b")
  } yield a ++ b

  val isParallel = Future("a") |@| Future("b")

}
