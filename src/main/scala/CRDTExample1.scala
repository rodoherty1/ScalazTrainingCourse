import scalaz.Monoid


trait BoundedSemiLattice[A] extends Monoid[A]

object BoundedSemiLattice {
  implicit object intInstance extends BoundedSemiLattice[Int] {
    override def zero: Int = Int.MinValue

    override def append(i: Int, j: => Int): Int = i max j
  }
}


object CRDTExample1 {

  import scalaz.syntax.monoid._
  import scalaz.std.anyVal._

  final case class GCounter[Id, Elt](map: scala.collection.Map[Id, Elt]) {
    def inc(id: Id, amt: Elt)(implicit m: Monoid[Elt]): GCounter[Id, Elt] = {
      GCounter(map.updated(id, map.getOrElse(id, m.zero) |+| amt))
    }

    def total(implicit m: Monoid[Elt]): Elt = map.values.foldLeft(m.zero)((b, elt) => b |+| elt)

    def merge(that: GCounter[Id, Elt])(implicit m: BoundedSemiLattice[Elt]): GCounter[Id, Elt] = {
      GCounter(
        that.map ++
        (for {
          (k, v) <- this.map
        } yield k -> (v |+| that.map.getOrElse(k, m.zero))))
    }
  }

  val g1 = GCounter(scala.collection.Map(1 -> 1, 2 -> 2))
  val g2 = GCounter(scala.collection.Map(1 -> 2, 2 -> 3))

  println(g1)
  println(g2)

  println(g1.inc(1, 2))
  assert(g1.inc(1, 2) equals GCounter(scala.collection.Map(1 -> 3, 2 -> 2)))

  println(g1.merge(g2))
  assert(g1.merge(g2) equals GCounter(scala.collection.Map(1 -> 2, 2 -> 3)))

  assert(g1.merge(g2).total equals 5)

}
