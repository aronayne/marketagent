package controllers


import play.api.mvc._
import play.api.libs.streams.ActorFlow
import javax.inject.Inject
import akka.actor.ActorSystem
import akka.http.scaladsl.model.ws.TextMessage
import akka.stream.Materializer
import akka.actor._

class Application @Inject()(cc: ControllerComponents)(implicit system: ActorSystem, mat: Materializer)
  extends AbstractController(cc) {
  def socket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef { out =>
      MyWebSocketActor.props(out)
    }
  }
}

object MyWebSocketActor {

  var counter = 0

  def props(out: ActorRef) = Props(new MyWebSocketActor(out))
}

class MyWebSocketActor(out: ActorRef) extends Actor {

  def receive = {
    case msg: String =>
      MyWebSocketActor.counter = MyWebSocketActor.counter + 1
      print("received message: " + msg + "," + MyWebSocketActor.counter)
      out ! "hello world! "
  }
}

