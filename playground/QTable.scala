//trait QTable
//case class QValues(elemnts : List[Double])
//case class State(elems : List[String]) extends QTable
//case class QValues(bindings : Map[String, List[Double]]) extends QTable

import java.io.InputStream
import javax.inject.Inject
import map.{CsvMapper, QTableRow}
import play.api.Environment
import scala.collection.mutable.ListBuffer
import scala.io.{BufferedSource, Source}

//class QTable {
//}
//
//object QTable extends App {
//
//    def getArgMaxValue(l : List[Double]) = {
//        l.zipWithIndex.maxBy(x => x._1)._2
//    }
//
//    val env = Environment.simple()
//    val is: InputStream = Option(env.classLoader.getResourceAsStream("q_table.txt")).get
//    val str = scala.io.Source.fromInputStream(is).mkString
//
//    val v = str.replace("\"" , "").replace("\'", "").replace("[", "").replace("]", "").split(",").toList
//    val v2 = v.grouped(10).toList
//    val v3 = v2.map(m => m(0).trim)
//    val v4 = v2.map(m => m.tail.map(x => x.toDouble))
//
//    val q = QValues(v3.zip(v4).toMap)
//
//    println(getArgMaxValue(q.bindings.get("OOX----X-").get))
//    println(getArgMaxValue(q.bindings.get("XOXOOXX--").get))
//    println(getArgMaxValue(q.bindings.get("---------").get))
//    println(getArgMaxValue(q.bindings.get("OXXXXO-OO").get))
//
//}

