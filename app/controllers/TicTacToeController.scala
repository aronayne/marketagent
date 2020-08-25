package controllers

import javax.inject._
import play.api.libs.json.Json
import play.api.mvc._
import java.io.InputStream
import javax.inject.Inject
import map.{CsvMapper, QTableRow}
import play.api.Environment
import scala.collection.mutable.ListBuffer
import scala.io.{BufferedSource, Source}
import scala.util.Random

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class TicTacToeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  val Q_TABLE_FILENAME = "q_table.txt"

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    Ok(views.html.tictactoe("Your new application is ready."))
  }

  /**
   * For a given board position return the position of the next move
   * @param player
   * @param board
   * @return
   */
  def getNextMovePosition(player: String, board: String) = Action { implicit request =>

    val rnd = new scala.util.Random
    val bboard = board.split("," , -1).map(m => {
      if(m.equals("")){
        "-"
      }
      else {
        m
      }
    }).mkString(",").replace("," , "")

    val ppp = board.split(",", -1).toList.zipWithIndex
    val remainingPositions : List[Int] = ppp.filterNot(f => f._1.equalsIgnoreCase("X") || f._1.equalsIgnoreCase("O")).map(x => x._2)

    val machinePosition = QTable(Q_TABLE_FILENAME).getArgMaxValue(bboard , remainingPositions)

    println("machinePosition "+machinePosition)
    Ok("{ \"success\":1 , \"positionValue\": "+machinePosition +"}")

  }
}

/**
 * Maps a state to the action with the arg max value
 * @param bindings
 */
case class QTable(bindings : Map[String , List[Double]]) {

  def getArgMaxValue(state: String , remainingPositions: List[Int]) = {
    val qValues = bindings.get(state).getOrElse(List.fill(8)(0.0)).zipWithIndex
    print("qValues"+qValues)
    val availableQValueBoardPositions = qValues.filter(f => remainingPositions.contains(f._2))
    println("toCheck"+availableQValueBoardPositions)
    availableQValueBoardPositions.maxBy(x => x._1)._2
  }

}

object Policy{

}

object QTable {

  //TODO Refactor in a single replace statement
  private def cleanData(data : String) : String = {
    data.replace("\"" , "").replace("\'", "").replace("[", "").replace("]", "")
  }

  private def getQTableFromFile(filename: String) = {
    lazy val env = Environment.simple()
    lazy val is: InputStream = Option(env.classLoader.getResourceAsStream(filename)).get
    scala.io.Source.fromInputStream(is).mkString
  }
  /**
   * Each row contains 1 state and 9 scalars. The scalars corresponds to the arg max values.
   * @param filename
   * @return
   */
    //TODO Refactor use of so many lazy vals and procedures
  def apply(filename : String) = {

      val number_attributes_per_instance = 10;

      val qtable = getQTableFromFile(filename)
      lazy val cleanedQtable : String = cleanData(qtable)
      lazy val dataInstances : List[List[String]] = cleanedQtable.split(",").toList.grouped(number_attributes_per_instance).toList
      lazy val stateIdValues : List[String] = dataInstances.map(m => m.head.trim)
      lazy val stateAttributeValues : List[List[Double]] = dataInstances.map(m => m.tail.map(x => x.toDouble))
      lazy val policy : QTable = new QTable(stateIdValues.zip(stateAttributeValues).toMap)

      policy
  }
}

object Main extends App {

  println(QTable("q_table.txt").getArgMaxValue("OOXXOX---" , List(6,7,8)))
//  println(QTable("q_table.txt").getArgMaxValue("XOXOOXX--"))
//  println(QTable("q_table.txt").getArgMaxValue("---------"))
//  println(QTable("q_table.txt").getArgMaxValue("OXXXXO-OO"))

}
