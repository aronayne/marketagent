package controllers

import java.lang
import java.util.concurrent.Semaphore

import controllers.GetD.tttC
import dto.Details
import javax.inject._
import org.bson.{BsonString, BsonValue}
import org.mongodb.scala.{Document, MongoClient, MongoCollection, MongoDatabase, Observer}
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.Await
import scala.concurrent.duration.Duration


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class ModelController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  lazy val database: MongoDatabase = MongoClient("mongodb://root:rootpassword@192.168.0.6:27017/admin").getDatabase("marketagent")

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {

    val tttC: MongoCollection[Document] = database.getCollection("tictactoe");
    val getO = tttC.find()

    val sem: Semaphore = new Semaphore(1)
    sem.acquire()

    val buf = scala.collection.mutable.ListBuffer.empty[Details]

    getO.subscribe(new Observer[Document] {
      override def onNext(document: Document): Unit = {

        val b : BsonValue = document.get("_id").get
        println("b is "+b.asObjectId().getValue.toString)

        buf += Details(document.getDouble("epsilon"), document.getDouble("discount"),
          document.getDouble("stepsize"), document.getDouble("averagereward"), document.getString("notes"),
          document.getInteger("numbergames"), document.getInteger("xgamewins"), document.getInteger("ogamewins"),
          document.getInteger("drawn"), b.asObjectId().getValue.toString,  document.getString("model_execution_start_time"))

      }
      override def onError(e: Throwable): Unit = println(s"onError: $e")
      override def onComplete(): Unit  = sem.release()
    })
    sem.acquire()

    Ok(views.html.modelconfigs(buf.toList))
  }

  def sj = Action {
      Ok(Json.toJson(List(1,2,3)).toString());
  }

}

import org.mongodb.scala._

import org.mongodb.scala.MongoClient
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.Document
import org.mongodb.scala.Observer
import org.mongodb.scala.MongoCollection

object GetD extends App {


  val mongoClient: MongoClient = MongoClient("mongodb://root:rootpassword@192.168.0.6:27017/admin")
  val database: MongoDatabase = mongoClient.getDatabase("marketagent")
  val tttC: MongoCollection[Document] = database.getCollection("tictactoe");
  val getO = tttC.find()

  import com.mongodb.BasicDBObject
  import org.bson.types.ObjectId

  val query = new BasicDBObject("_id", new ObjectId("5ee9f6d2f2da318b773a2f1c"))

  val get1 = tttC.find(query)

  getO.subscribe(new Observer[Document] {
    override def onNext(document: Document): Unit = println(document.get("_id").get.asObjectId().getValue)
    override def onError(e: Throwable): Unit = println(s"onError: $e")
    override def onComplete(): Unit = println("onComplete")
  })
  Thread.sleep(3000)

  get1.subscribe(new Observer[Document] {
    override def onNext(document: Document): Unit = println(document)
    override def onError(e: Throwable): Unit = println(s"onError: $e")
    override def onComplete(): Unit = println("onComplete")
  })
  Thread.sleep(3000)

}

