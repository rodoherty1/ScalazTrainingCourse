import java.io.{BufferedOutputStream, FileOutputStream}

import scala.io.{BufferedSource, Source}
import scalaz.concurrent.Task
import scalaz.stream._


object Streams extends App {


  val open  = Task.delay { Source.fromFile("rob.txt") }

  val close = (src: BufferedSource) => Process.eval(Task.delay(src.close())).drain

  val read: BufferedSource => Process[Task, String] = src => {
    val iterator = src.getLines()

    def loop: Process[Task, String] = {
      if (iterator.hasNext) {
        val first: Process[Task, String] = Process.emit(iterator.next())
        //val rest: Process[Task, String] = Process.eval(Task.delay(loop)).flatMap(x => x)

        first ++ loop
      }
      else Process.halt
    }

    loop
  }

  def readLines(fileName: String): Process[Task, String] = {
    val onStart: Task[BufferedSource] = Task.delay { scala.io.Source.fromFile(fileName)}

    Process.bracket(open)(close)(read)
  }

  println(readLines("rob.txt").runLog.run)



  def writeLines(fileName: String): Process[Task, Unit] = {

    val open: Task[BufferedOutputStream] = {
      Task.delay(new BufferedOutputStream(new FileOutputStream(fileName)))
    }

    val close = (bos: BufferedOutputStream) => {
      Process.eval(Task.delay(bos.close())).drain
    }

    val write = (bos: BufferedOutputStream) => {
      val t: Task[Unit] = Task.delay { (line: String) => {
          bos.write(line.getBytes)
          bos.write("\n".getBytes)
        }
      }

      Process.eval (t)
    }

    Process.bracket(open)(close)(write)
  }

  println(writeLines("rob2.txt").runLog.run)



  /*
  Noel said .... if writeLines is a Sink[Task, String] then we can hook up readLines using

  readLines to writeLines

  */

}
