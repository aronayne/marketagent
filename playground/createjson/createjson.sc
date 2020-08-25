import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.{DefaultScalaModule, ScalaObjectMapper}

import scala.util.{Failure, Success, Try}

def toJson[T](obj: T)(implicit m: Manifest[T]): Option[String] = {
  Try {
    lazy val mapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    mapper.writeValueAsString(obj)
  } match {
    case Success(x) => Some(x)
    case Failure (err) => {
      println("error: @@@@Got " + err.getMessage() + " while converting object  to JSON:--> " + obj)
      None
    }
  }
}

case class Response(timestamp : String, price: String)

val r = scala.util.Random

toJson(Response(String.valueOf(System.currentTimeMillis()) , String.valueOf(r.nextFloat())))