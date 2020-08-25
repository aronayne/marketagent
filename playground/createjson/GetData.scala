package createjson

import java.util.concurrent.TimeUnit

import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import akka.actor.typed.{ActorSystem, Behavior}
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.{DefaultScalaModule, ScalaObjectMapper}

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

object PrintMyActorRefActor {
  def apply(): Behavior[DataType] =
    Behaviors.setup(context => new PrintMyActorRefActor(context))
}

class PrintMyActorRefActor(context: ActorContext[DataType]) extends AbstractBehavior[DataType](context) {

  override def onMessage(msg: DataType): Behavior[DataType] =
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

  var data : Option[String] = Option.empty

  def setData(data : Option[String]): Unit ={
    this.data = data
  }

  def getData(): Option[String] ={
    this.data
  }

}

class Main(context: ActorContext[String]) extends AbstractBehavior[String](context) {

  case class Response(timestamp : String, price: String)

  def toJson[T](obj: T)(implicit m: Manifest[T]): Option[String] = {
    Try {
      lazy val mapper = new ObjectMapper() with ScalaObjectMapper
      mapper.registerModule(DefaultScalaModule)
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      mapper.writeValueAsString(obj)
    } match {
      case Success(x) => Some(x)
      case Failure (err) => {
        println("error: @@@@Got " + err.getMessage() + " while converting object  to JSON:--> " + obj)
        None
      }
    }
  }

  override def onMessage(msg: String): Behavior[String] =
    msg match {
      case "getdata" =>

        val r = scala.util.Random

        val firstRef = context.spawn(PrintMyActorRefActor(), "first-actor" + String.valueOf(System.currentTimeMillis()))
        println(s"First: $firstRef")

//        val url = "http://adrianronayne.pythonanywhere.com/btc-eur?cur_param=2020-06-04"
//        val result = scala.io.Source.fromURL(url)
//
//        var data = new ListBuffer[String]()
//
//        for (line <- result.getLines.toList.tail) {
//          data += line.split(",")(0)
//        }

        val d = new DataType("1")
        d.setData(toJson(Response(String.valueOf(System.currentTimeMillis()) , String.valueOf(r.nextFloat()))))

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