
object Monoid1 {
  import scalaz.syntax.monoid._
  import scalaz.std.anyVal._
  import scalaz.std.list._
  import scalaz.std.option._

  val two   = 1 |+| 1

  val lists = List(1, 2, 3) |+| List(4, 5, 6)

  val options = Option(1) |+| Option(2)

  val optionLists = Option(List(1, 2, 3)) |+| Option(List(4, 5, 6))

  val s: Some[Int] = Some(1)

  val tricky = Option(1) |+| Option(2)

}




