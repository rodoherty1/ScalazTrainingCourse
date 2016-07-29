import java.util.concurrent.ArrayBlockingQueue

import scalaz.Applicative

object Value {
  implicit object valueInstance extends Applicative[Value] {
    override def point[A](a: => A): Value[A] = Emit(a)

    override def ap[A, B](fa: => Value[A])(ff: => Value[(A) => B]): Value[B] = {
      ff match {
        case Emit(f) =>
          fa match {
            case Emit(a) => Emit(f(a))
            case Done() => Done()
          }

        case Done() => Done()
      }
    }
  }
}

sealed trait Value[A] extends Product with Serializable {
  def map[B](f: A => B) : Value[B] = this match {
    case Emit(a) => Emit(f(a))
    case Done()  => Done()
  }

  def flatMap[B](f: A => Value[B]): Value[B] = this match {
    case Emit(a) => f(a)
    case Done()  => Done()
  }
}

final case class Emit[A](a: A) extends Value[A]

final case class Done[A]() extends Value[A]

sealed trait EventStream[A] extends Product with Serializable {

  def map[B](f: A => B): EventStream[B] = Map(this, f)

  def zip[B](that: EventStream[B]): EventStream[(A, B)] = Zip(this, that)

  def foldLeft[B](initialValue: B)(f: (B, A) => B): Sink[A, B] =
    Sink(this, initialValue, f)

  def step: Value[A]
}

final case class Map[A, B](source: EventStream[A], f: A => B) extends EventStream[B] {
  override def step: Value[B] = source.step.map(f)
}

final case class Zip[A, B](left: EventStream[A], right: EventStream[B]) extends EventStream[(A, B)] {
  import scalaz.syntax.applicative._ // for |@|

  override def step: Value[(A, B)] = (left.step |@| right.step).tupled
}

final case class Pure[A](a: A) extends EventStream[A] {
  override def step: Value[A] = Emit(a)
}

object EventStreamExample {
  def run() = {
    val register =
      (callback: Value[Int] => Unit) => {
        Driver.run(5, callback) // When register is called with Value[Int] => Unit, then start the Driver
      }

    val source = Callback(register)

    source.foldLeft(0)((z, a) => z + a).step
  }

  def pureDerp = Pure(1).foldLeft(0){_ + _}.step
}

object Driver{
  def run(limit: Int, callback: Value[Int] => Unit): Unit = {
    new Thread {
      var counter = 0
      override def run(): Unit = {
        while(counter < limit) {
          println (s"Thread generating $counter")
          callback(Emit(counter))
          counter = counter + 1
        }
        callback(Done())
      }
    }.start()
  }
}

final case class Sink[A, B](source: EventStream[A], initialValue: B, f: (B, A) => B) {
  def step: B = {
    def loop(currentValue: B): B = {
      source.step match {
        case Emit(a) => loop(f(currentValue, a))
        case Done()  => currentValue
      }
    }
    loop(initialValue)
  }
}

final case class Callback[A](register: (Value[A] => Unit) => Unit) extends EventStream[A] {
  val queue = new ArrayBlockingQueue[Value[A]](1)

  var registered = false

  override def step: Value[A] = {
    if (!registered) {
      register((a: Value[A]) => queue.put(a))
      registered = true
    }

    queue.take()
  }
}

