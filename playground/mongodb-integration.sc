import org.mongodb.scala._

val mongoClient: MongoClient = MongoClient()

val database: MongoDatabase = mongoClient.getDatabase("marketagent")
val tttC: MongoCollection[Document] = database.getCollection("tictactoe");

val getO = tttC.find()

getO.subscribe(new Observer[Document] {
  override def onNext(document: Document): Unit = println(document.toJson())
  override def onError(e: Throwable): Unit = println(s"onError: $e")
  override def onComplete(): Unit = println("onComplete")
})

//import netscape.javascript.JSObject
//import org.bson.Document


//val data: String = "\' this is a test \\ "
//
//val ddd : List[Char] = data.toCharArray.foldLeft(List.empty[Char]) { (acc, c) =>
//  c match {
//    case '\'' => acc :+ '_'
//    case '\\' => acc :+ '_'
//    case other => acc :+ other
//  }
//}
//
//print(ddd.mkString(""))

//import org.bson.codecs.configuration.CodecRegistries.fromCodecs

//val data : String = "\' this is a test \\ "
//
//data.toCharArray.foldLeft("") { (result, ch) =>
//  if (ch == '\'' || ch == '\\') result
//  else result + ch
//}


//val collection: MongoCollection[Document] = database.getCollection("tictactoe");
//
//val doc: Document = Document("_id" -> 2, "name" -> "MongoDB", "type" -> "database",
//  "count" -> 1, "info" -> Document("x" -> 203, "y" -> 102))
//
////val s : SingleObservable[Completed] = collection.insertOne(doc)
//
////https://mongodb.github.io/mongo-scala-driver/1.0/reference/crud/
//
//val document: Document = Document("_id" -> 10, "x" -> 1)
//val insertObservable: Observable[Completed] = collection.insertOne(document)
//
//insertObservable.subscribe(new Observer[Completed] {
//  override def onNext(result: Completed): Unit = println(s"onNext: $result")
//  override def onError(e: Throwable): Unit = println(s"onError: $e")
//  override def onComplete(): Unit = println("onComplete")
//})



//collection.subscribe((doc: Document) => println(doc.toJson()))

//val s : SingleObservable[Completed] = collection.insertOne(doc)

//collection.insertOne(doc).subscribe(new Observer[Document](){
//
//  var batchSize: Long = 10
//  var seen: Long = 0
//  var subscription: Option[Subscription] = None
//
//  override def onSubscribe(subscription: Subscription): Unit = {
//    this.subscription = Some(subscription)
//    subscription.request(batchSize)
//  }
//
//  override def onNext(result: Document): Unit = {
////    println(document.toJson())
//    println("onNext")
//    seen += 1
//    if (seen == batchSize) {
//      seen = 0
//      subscription.get.request(batchSize)
//    }
//  }
//
//  override def onError(e: Throwable): Unit = println(s"Error: $e")
//
//  override def onComplete(): Unit = println("Completed")
//})


//val client:MongoClient=MongoClient()
//
//val db: MongoDatabase = client.getDatabase("marsketagent")
//
//val collection: MongoCollection[Document] = db.getCollection("test");
//
//collection.find().pr


//val js : MongoCollection[JSObject] = db.getCollection("tictsactoe")
