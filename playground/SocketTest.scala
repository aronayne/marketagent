import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.ws._
import akka.stream.scaladsl._
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.{Done, NotUsed}

import scala.concurrent.Future

object WebSocketClientFlow {
  def main(args: Array[String]) = {

    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    import system.dispatcher

    val req = WebSocketRequest(uri = "ws://localhost:9000/ws")
    val webSocketFlow = Http().webSocketClientFlow(req)

    val messageSource: Source[Message, ActorRef] =
      Source.actorRef[TextMessage.Strict](bufferSize = 10, OverflowStrategy.fail)

    val messageSink: Sink[Message, NotUsed] =
      Flow[Message]
        .map(message => println(s"Received text message: [$message]"))
        .to(Sink.ignore)

    val ((ws, upgradeResponse), closed) =
      messageSource
        .viaMat(webSocketFlow)(Keep.both)
        .toMat(messageSink)(Keep.both)
        .run()

    val connected = upgradeResponse.flatMap { upgrade =>
      if (upgrade.response.status == StatusCodes.SwitchingProtocols) {
        Future.successful(Done)
      } else {
        throw new RuntimeException(s"Connection failed: ${upgrade.response.status}")
      }
    }

    ws ! TextMessage.Strict("Hello World")
    ws ! TextMessage.Strict("Hi")
    ws ! TextMessage.Strict("Yay!")

  }
}
