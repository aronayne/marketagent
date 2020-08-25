package services

import org.mongodb.scala.{Document, MongoClient, MongoCollection, MongoDatabase}

class DB {

}

object DB {

  val dbConnectionString = "mongodb://root:rootpassword@192.168.0.6:27017/admin"

  def getTicTacToeDocument = {
    lazy val database: MongoDatabase = MongoClient(dbConnectionString).getDatabase("marketagent")
    lazy val tttC: MongoCollection[Document] = database.getCollection("tictactoe");

    tttC

  }
}
