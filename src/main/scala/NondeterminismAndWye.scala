import scalaz.concurrent.Task
import scala.concurrent.duration._
import scalaz.Nondeterminism
import scalaz.stream.{Process, wye}

object NondeterminismAndWye extends App {

  def fastAndSlow(): Unit = {
    val task1 = Task.schedule( "Slow Result", 5.seconds)
    val task2 = Task.delay( "Fast Result")

    println(task1.flatMap(res1 => task2.flatMap(res2 => Task.now(s"$res1, $res2"))).run)

    println(Nondeterminism[Task].both(task1, task2).run)
  }

  def fastAndNotAtAll (): Unit = {
    val task1: Task[String] = Task.schedule( {println("task1"); "Slow Result"}, 10.seconds)
    val task2: Task[String] = Task.schedule( {println("task2"); "Fast Result"}, 5.seconds)

    val p: Process[Task, String] = Process.eval(task1).wye(Process.eval(task2))(wye.merge)

    println(p.take(1).run.run)
  }

  fastAndSlow()
  fastAndNotAtAll()
}
