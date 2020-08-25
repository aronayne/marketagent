import java.io.File

import play.api.test
import play.api.test.Helpers
import play.api.mvc._
import javax.inject._
import org.specs2.mutable.Specification
import play.api.Environment
import play.api.mvc.{AbstractController, ControllerComponents}

class CountController @Inject() (cc: ControllerComponents,
                                 env: Environment) extends AbstractController(cc) {

  def getter() = Option(env.classLoader.getResourceAsStream(".csv"))

}


class Tester extends Specification {


  "Math" should {
    "add" in {

      println("here"+new CountController(play.api.test.Helpers.stubControllerComponents(), Environment.simple()).getter())

      1+2 === 3
    }
  }

//  def test2 = {
//    println(new CountController(play.api.test.Helpers.stubControllerComponents(), Environment.simple()).getter())
//  }



}
