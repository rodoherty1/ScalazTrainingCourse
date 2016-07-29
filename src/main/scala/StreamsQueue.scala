
import java.util.concurrent.Executors

import scalaz.concurrent.{Strategy, Task}
import scalaz.stream._
import scalaz.stream.async
import scalaz.stream.async.mutable.{Queue, Topic}

object StreamsQueue extends App {


  val mySink = sink.lift[Task, String]((s: String) =>
    Task.delay {
      println(s)
    }
  )

  val myQueue: Queue[String] = async.boundedQueue[String](10)

//  new Thread {
//    override def run(): Unit = {
//      def loop: Unit = {
//        val line = scala.io.StdIn.readLine()
//        if (line == "bye") {
//          myQueue.close.run
//          ()
//        } else {
//          myQueue.enqueueOne(line).run
//          loop
//        }
//      }
//      loop
//    }
//  }.start()

  //Process.repeat(myQueue.dequeue to mySink).run.run


  val readLine: Process[Task, String] = Process.repeatEval {
    Task.delay {
      scala.io.StdIn.readLine()
    }
  }

  val myBot: Channel[Task, String, String] =
    Process.constant { (s: String) => Task.delay {
        "Howdy"
      }
    }

  val mySpyingRoom = sink.lift[Task, String] {s => Task.delay { println ("We're watching you!") } }



  val room: Topic[String] = async.topic[String](readLine)



  val p = merge.mergeN(2){
    Process(
      readLine to myQueue.enqueue,
      room.subscribe to mySpyingRoom,
      myQueue.dequeue to mySink
    )
  }(Strategy.Executor(Executors.newFixedThreadPool(4)))

  p.run.run
}
