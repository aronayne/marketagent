package dto

case class PricingData(id: String) {

  def getId(): String = {
    this.id
  }

  var data: Option[String] = Option.empty

  def setData(data: Option[String]): Unit = {
    this.data = data
  }

  def getData(): Option[String] = {
    this.data
  }

}
