import java.io.InputStream

import javax.inject.Inject
import map.QTableRow
import play.api.Environment

import scala.collection.mutable.ListBuffer
import scala.io.BufferedSource
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import scala.util.{Failure, Success, Try}

object JsonUtil {
  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

  def toJson(value: Map[Symbol, Any]): String = {
    toJson(value map { case (k,v) => k.name -> v})
  }

  def toJson(value: Any): String = {
    mapper.writeValueAsString(value)
  }

  def toMap[V](json:String)(implicit m: Manifest[V]) = fromJson[Map[String,V]](json)

  def fromJson[T](json: String)(implicit m : Manifest[T]): T = {
    mapper.readValue[T](json)
  }

  def fromJson2[T](json: String)(implicit m: Manifest[T]): Option[T] = {
    Try {
      lazy val mapper = new ObjectMapper() with ScalaObjectMapper
      mapper.registerModule(DefaultScalaModule)
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      mapper.readValue[T](json)
    } match {
      case Success(x) => Some(x)
      case Failure(err) => {

        println(err.getMessage)
//        logger.error("@@@@Got " + err.getMessage() + " while JSON to Object:--> " + json)
        None
      }
    }
  }
}

  class CsvMapper2 @Inject()(env: Environment) {
    def getQTable2() = {

      val is: InputStream = Option(env.classLoader.getResourceAsStream("c-data.json")).get

//      print("is"+is)
      val bufferedSource: String = scala.io.Source.fromInputStream(is).mkString

      println("bufferedSource:"+bufferedSource)

      println(JsonUtil.fromJson2(bufferedSource))


    }

  }

  object main extends App {

    //  val : List[QTableRow]

    println("here")
    new CsvMapper2(Environment.simple()).getQTable2()
    //  val l = List(QTableRow.argValue1, argValue1, argValue2, argValue3, argValue4, argValue5, argValue6, argValue7, argValue8)

    //  l = List()
    //  QTableRow.get
//    val qr : QTableRow = qTable.filter(f => f.stateString.equals("202222222"))(0)
//
//    println(qr.argValues)
//
//    println(qr.getArgMaxValue)

  }



