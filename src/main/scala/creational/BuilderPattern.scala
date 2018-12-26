package creational

object api {
  final case class Car(id: String,
                       make: String,
                       model: String,
                       seats: Int,
                       sportsFeature: Boolean)
}

object BuilderPatternClient {
  import api._

  def main(args: Array[String]): Unit = {
    val car = Car(
      id = "1",
      make = "TOYOTA",
      model = "CAMRY",
      seats = 2,
      sportsFeature = true
    )
  }
}
