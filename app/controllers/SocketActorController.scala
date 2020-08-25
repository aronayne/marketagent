package controllers

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorRef, Props, _}
import akka.stream.Materializer
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.{DefaultScalaModule, ScalaObjectMapper}
import dto.PricingData
import javax.inject.Inject
import map.{CsvMapper, MarketResultsRow}
import play.api.Environment
import play.api.libs.streams.ActorFlow
import play.api.mvc.{AbstractController, ControllerComponents, WebSocket}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

class SAApplication @Inject()(cc: ControllerComponents)(implicit system: akka.actor.ActorSystem, mat: Materializer)
  extends AbstractController(cc) {
  def socket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef { out =>
      PricingDetailsWebSocket.props(out)
    }
  }
}

object PricingDetailsWebSocket {

  def props(out: ActorRef) = Props(new PricingDetailsWebSocket(out))

}

class PricingDataDownloadActor extends Actor {

  case class Response(timestamp: String, price: String, modelAction: String, bestAction: String)

  private def toJson[T](obj: T)(implicit m: Manifest[T]): Option[String] = {
    Try {
      lazy val mapper = new ObjectMapper() with ScalaObjectMapper
      mapper.registerModule(DefaultScalaModule)
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      mapper.writeValueAsString(obj)
    } match {
      case Success(x) => Some(x)
      case Failure(err) => {
        println("error: @@@@Got " + err.getMessage() + " while converting object  to JSON:--> " + obj)
        None
      }
    }
  }

  override def receive: Receive = {
//    (MeanPrice, Model Action Taken, Best Action Taken)
    case StartMessage(dataPoint : (Double , String , String)) =>
      println("StartMessage: "+dataPoint)

      val d = new PricingData("1")
      val r = scala.util.Random
      d.setData(toJson(Response(String.valueOf(System.currentTimeMillis()), String.valueOf(dataPoint._1) , dataPoint._2 , dataPoint._3)))

      sender() ! d
  }

}

object DataGet {

 def getM() = {
    new CsvMapper(Environment.simple()).getPolicy().map(x => x.meanPrice);
 }
  def getMA() = {
    new CsvMapper(Environment.simple()).getPolicy().map(x => x.modelAction);
  }
  def getBA() = {
    new CsvMapper(Environment.simple()).getPolicy().map(x => x.bestAction);
  }

//val modelActions : List[String] = new CsvMapper(Environment.simple()).getPolicy().map(x => x.modelAction);
//val bestActions : List[String] = new CsvMapper(Environment.simple()).getPolicy().map(x => x.bestAction);
//
}
//(MeanPrice, Model Action Taken, Best Action Taken)
case class StartMessage(dataPoint : (Double, String, String))

case object Finish

class PricingDetailsWebSocket(out: ActorRef) extends Actor {

//  val modelResults : List[MarketResultsRow]  = new CsvMapper(Environment.simple()).getPolicy()

  val pricesData = DataGet.getM()
  val modelActions = DataGet.getMA()
  val bestActions = DataGet.getBA()

  var index = 0
  def getNextElement() : (Double, String, String) = {
    index = index + 1
    (pricesData(index - 1) , modelActions(index - 1)  ,bestActions(index - 1))
  }

  val scheduler = context.system.scheduler
  val toggle = context.actorOf(Props[PricingDataDownloadActor])
  val downloadTask = new Runnable {
    def run() {
      toggle ! StartMessage(getNextElement())
    }
  }
  scheduler.scheduleAtFixedRate(
    initialDelay = Duration(0, TimeUnit.SECONDS),
    interval = Duration(1, TimeUnit.SECONDS))(
    runnable = downloadTask)

  def receive = {
    case msg: String =>
      out ! "hello world! "

    case d: PricingData =>
      print("case d : DataType =>")

      // Send the datatype content to the client connected to socket
      out ! d.data.get
      println(d.data.get)
  }

  //TODO Throttle requests to minimise AWS costs.
}

object main extends App {
  DataGet.getMA().foreach(println)
}