package controllers

import java.util
import java.util.concurrent.Semaphore

import dto.Details
import javax.inject._
import org.bson.BsonValue
import org.mongodb.scala.{Document, MongoClient, MongoCollection, MongoDatabase, Observer}
import play.api.libs.json.Json
import play.api.mvc._
import com.mongodb.BasicDBObject
import org.bson.types.ObjectId
import scala.collection.JavaConverters._
import scala.collection.mutable

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class ResultsController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  lazy val database: MongoDatabase = MongoClient("mongodb://root:****@192.168.0.6:27017/admin").getDatabase("marketagent")
  val tttC: MongoCollection[Document] = database.getCollection("tictactoe");

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index(id : String) = Action {

    println("id to select on is"+id)


    val query = new BasicDBObject("_id", new ObjectId(id))
    val get1 = tttC.find(query)

    val buf = scala.collection.mutable.ListBuffer.empty[Details]

//    Lock to create a blocking call
    val sem: Semaphore = new Semaphore(1)
    sem.acquire()

    var l : List[BsonValue] = List.empty

    get1.subscribe(new Observer[Document] {
      override def onNext(document: Document): Unit = {
        l = document.get("reward_per_episode").get.asArray().getValues.asScala.toList
      }

      override def onError(e: Throwable): Unit = println(s"onError: $e")

      override def onComplete(): Unit = sem.release()
    })
    sem.acquire()

    val s = l.map(m => m.asInt32().getValue).grouped(200).map(_.sum).toList

    println("values are"+l.map(m => m.asInt32().getValue))
      Ok(views.html.results(List.range(1, s.size) ,s))
  }
    //    val tttC: MongoCollection[Document] = database.getCollection("tictactoe")
//    val getO = tttC.find()
//
//    val sem: Semaphore = new Semaphore(1)
//    sem.acquire()
//
//    val buf = scala.collection.mutable.ListBuffer.empty[Details]
//
//    getO.subscribe(new Observer[Document] {
//      override def onNext(document: Document): Unit = {
//
//        val b : BsonValue = document.get("_id").get
//        println("b is "+b.asObjectId().getValue.toString)
//
//        buf += Details(document.getDouble("epsilon"), document.getDouble("discount"),
//          document.getDouble("stepsize"), document.getDouble("averagereward"), document.getString("notes"),
//          document.getInteger("numbergames"), document.getInteger("xgamewins"), document.getInteger("ogamewins"),
//          document.getInteger("drawn"), b.asObjectId().getValue.toString)
//
//      }
//      override def onError(e: Throwable): Unit = println(s"onError: $e")
//      override def onComplete(): Unit  = sem.release()
//    })
//    sem.acquire()

//    Ok(views.html.modelconfigs(buf.toList))



//  def sj = Action {
//      Ok(Json.toJson(List(1,2,3)).toString());
//  }

}

object ResultsControllerApp extends App {


  val mongoClient: MongoClient = MongoClient("mongodb://root:rootpassword@192.168.0.6:27017/admin")
  val database: MongoDatabase = mongoClient.getDatabase("marketagent")
  val tttC: MongoCollection[Document] = database.getCollection("tictactoe");
  val query = new BasicDBObject("_id", new ObjectId("5eea8920f2da318b773a2f1e"))
  val get1 = tttC.find(query)

  get1.subscribe(new Observer[Document] {
    override def onNext(document: Document): Unit = {
      val l: List[BsonValue] = document.get("reward_per_episode").get.asArray().getValues.asScala.toList

      println("here: "+l.map(m => m.asInt32().getValue))

    }
    override def onError(e: Throwable): Unit = println(s"onError: $e")
    override def onComplete(): Unit = println("onComplete")
  })
  Thread.sleep(3000)

}





