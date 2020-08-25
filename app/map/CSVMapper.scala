package map

import java.io.{File, InputStream}
import scala.collection.mutable.ListBuffer
import javax.inject._
import play.api.Environment
import scala.io.BufferedSource

trait QTableRow {

  val id: String
  val stateString: String
  val argValue0: Double
  val argValue1: Double
  val argValue2: Double
  val argValue3: Double
  val argValue4: Double
  val argValue5: Double
  val argValue6: Double
  val argValue7: Double
  val argValue8: Double

  def argValues: List[Double]

  def getArgMaxValue: Int
}

trait MarketResultsRow {
  val bestAction : String
  val modelAction : String
  val meanPrice : Double
}

object MarketResultsRow{
  def apply(modelActionParam: String, bestActionParam:String, meanPriceParam:Double) = new MarketResultsRow {
    override val modelAction: String = modelActionParam
    override val bestAction: String = bestActionParam
    override val meanPrice: Double = meanPriceParam

    override def toString(): String = {
      return modelAction + "," + bestAction + "," + meanPrice
    }

  }
}

object QTableRow {

  def apply(param1: String, param2: String, param3: String, param4: String, param5: String, param6: String,
            param7: String, param8: String, param9: String, param10: String, param11: String) = new QTableRow {

    override val id: String = param1
    override val stateString: String = param2
    override val argValue0: Double = param3.toDouble
    override val argValue1: Double = param4.toDouble
    override val argValue2: Double = param5.toDouble
    override val argValue3: Double = param6.toDouble
    override val argValue4: Double = param7.toDouble
    override val argValue5: Double = param8.toDouble
    override val argValue6: Double = param9.toDouble
    override val argValue7: Double = param10.toDouble
    override val argValue8: Double = param11.toDouble

    override def toString(): String = {
      return id + "," + stateString + "," + argValue0 + "," + argValue1 + "," + argValue2 + "," +
        argValue3 + "," + argValue4 + "," + argValue5 + "," + argValue6 + "," + argValue7 + "," + argValue8
    }

    override def argValues: List[Double] = List(argValue0, argValue1, argValue2, argValue3, argValue4, argValue5, argValue6,
      argValue7, argValue8)

    override def getArgMaxValue() = {

      val l = List(argValue0, argValue1, argValue2, argValue3, argValue4, argValue5, argValue6,
        argValue7, argValue8)

      l.zipWithIndex.maxBy(x => x._1)._2
    }

  }

}

class CsvMapper @Inject()(env: Environment) {

  def getPolicy() = {

    val is: InputStream = Option(env.classLoader.getResourceAsStream("policies/dqn1594665041.600363.csv")).get

    val bufferedSource: BufferedSource = scala.io.Source.fromInputStream(is)

//    val liness = bufferedSource.getLines.toList.tail(0).split(",").toList.map(_.trim)

    var tableRow = new ListBuffer[MarketResultsRow]()

    val lines : List[String] = bufferedSource.getLines.toList.tail

    for (line <- lines) {
      val cols = line.split(",").toList.map(_.trim)

      val modelAction = cols(1).toString

      val bestAction = cols(2).toString

      val meanPrice = cols(4).toDouble

      tableRow += MarketResultsRow(modelAction, bestAction, meanPrice)

    }

    bufferedSource.close
    tableRow.toList

  }

  def getTicTacToeQTable() = {

    val is: InputStream = Option(env.classLoader.getResourceAsStream("qtables/tictactoe/q_table_df_xo1590957329.088215.csv")).get

    val bufferedSource: BufferedSource = scala.io.Source.fromInputStream(is)

    var tableRow = new ListBuffer[QTableRow]()

    for (line <- bufferedSource.getLines.toList.tail) {
      val cols = line.split(",").map(_.trim)
      tableRow += QTableRow(cols(0), cols(1), cols(2), cols(3), cols(4), cols(5), cols(6), cols(7), cols(8), cols(9), cols(10))
    }

    bufferedSource.close
    tableRow.toList

  }

}

object main extends App {

//  val : List[QTableRow]

  val qTable : List[QTableRow]  = new CsvMapper(Environment.simple()).getTicTacToeQTable()
//  val l = List(QTableRow.argValue1, argValue1, argValue2, argValue3, argValue4, argValue5, argValue6, argValue7, argValue8)

//  l = List()
//  QTableRow.get
  val qr : QTableRow = qTable.filter(f => f.stateString.equals("202222222"))(0)

  println(qr.argValues)

  println(qr.getArgMaxValue)

  val modelResults : List[MarketResultsRow]  = new CsvMapper(Environment.simple()).getPolicy()

  val prices = new CsvMapper(Environment.simple()).getPolicy().map(x => x.meanPrice);

  modelResults.foreach(println)

}



