import java.io.InputStream
import play.api.Environment

case class QTable(bindings: Map[String, List[Double]]) {

  def getArgMaxValue(state: String, remainingPositions: List[Int]) = {
    val qValues = bindings.get(state).getOrElse(List.fill(8)(0.0)).zipWithIndex
    val availableQValueBoardPositions = qValues.filter(f => remainingPositions.contains(f._2))
    availableQValueBoardPositions.maxBy(x => x._1)._2
  }

}

object QTable {

  private def getQTableFromFile(filename: String) = {
    lazy val env = Environment.simple()
    lazy val is: InputStream = Option(env.classLoader.getResourceAsStream(filename)).get
    scala.io.Source.fromInputStream(is).mkString
  }

  private def cleanData(data: String): String = {


    data.replace("\"", "").replace("\'", "").replace("[", "").replace("]", "")
  }

  def apply(filename: String) = {

    val number_attributes_per_instance = 10;

    val qtable = getQTableFromFile(filename)

    lazy val cleanedQtable: String = cleanData(qtable)
    lazy val dataInstances: List[List[String]] = cleanedQtable.split(",").toList.grouped(number_attributes_per_instance).toList
    lazy val stateIdValues: List[String] = dataInstances.map(m => m.head.trim)
    lazy val stateAttributeValues: List[List[Double]] = dataInstances.map(m => m.tail.map(x => x.toDouble))
    lazy val policy: QTable = new QTable(stateIdValues.zip(stateAttributeValues).toMap)

    policy
  }

}

object ArgMain extends App {
  print(QTable("q_table_test.txt").getArgMaxValue("---------", List(1, 2, 3)))
}

/*

Here is my implementation of arg max in Scala. q_table_test.txt contains:
["---------,'0','0','0','0','0','0','0','0','0', "X--------,'0.', '0.1', '0.1', '0.1', '0.1', '0.1', '0.1', '0.1', '0.1'"]

For example the String ---------,'0','0','0','0','0','0','0','0','0' maps to key: "---------" , with values: [0,0,0,0,0,0,0,0,0]
After filtering values which are not valid I return the arg max of values from the list of positions.

Coming from a java background, what is a more functional programming principled method of implementing arg max? Should the
list filtering be contained in a separate function?

The QTable apply() method parses reads a file and returns Map[String , List[Double]] . It is evaluated lazily as it should
just be evaluated once.

I think the use of apply in lazy val is not correct ?

Is this a 'good' method of finding the arg max value ?:

    val qValues = bindings.get(state).getOrElse(List.fill(8)(0.0)).zipWithIndex
    val availableQValueBoardPositions = qValues.filter(f => remainingPositions.contains(f._2))




 */