import java.util.concurrent.TimeUnit

import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import akka.actor.typed.{ActorSystem, Behavior}

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object PrintMyActorRefActor {
  def apply(): Behavior[getdata.DataType] =
    Behaviors.setup(context => new getdata.PrintMyActorRefActor(context))
}

class PrintMyActorRefActor(context: ActorContext[getdata.DataType]) extends AbstractBehavior[getdata.DataType](context) {

  override def onMessage(msg: getdata.DataType): Behavior[getdata.DataType] =
    msg.getId() match {
      case "1" => // ignores all but the head element
        val secondRef = context.spawn(Behaviors.empty[String], "second-actor")
        println(s"Second: $secondRef")
        println("msg is"+msg.getData())
        this
    }
}

object Main {
  def apply(): Behavior[String] =
    Behaviors.setup(context => new Main(context))

}

case class DataType(id : String){

  def getId(): String = {
    this.id
  }

  var data : List[String] = List()

  def setData(data : List[String]): Unit ={
    this.data = data
  }

  def getData(): List[String] ={
    this.data
  }

}

class Main(context: ActorContext[String]) extends AbstractBehavior[String](context) {
  override def onMessage(msg: String): Behavior[String] =
    msg match {
      case "getdata" =>

        val firstRef = context.spawn(getdata.PrintMyActorRefActor(), "first-actor" + String.valueOf(System.currentTimeMillis()))
        println(s"First: $firstRef")

        val url = "http://adrianronayne.pythonanywhere.com/btc-eur?cur_param=2020-06-04"
        val result = scala.io.Source.fromURL(url)

        var data = new ListBuffer[String]()

        for (line <- result.getLines.toList.tail) {
          data += line.split(",")(0)
        }

        val d = new getdata.DataType("1")
        d.setData(data.toList)

        firstRef ! d
        this
    }
}

object ActorHierarchyExperiments extends App {
  val testSystem = ActorSystem(Main(), "testSystem")
  val scheduler = testSystem.scheduler

  val task = new Runnable {
    def run() {
      testSystem ! "getdata"
    }
  }

  scheduler.scheduleAtFixedRate(
    initialDelay = Duration(0, TimeUnit.SECONDS),
    interval = Duration(3, TimeUnit.SECONDS))(
    runnable = task)
}