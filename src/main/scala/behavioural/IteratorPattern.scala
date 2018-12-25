package behavioural

import java.time.{LocalDateTime, ZoneOffset}

/**
  * Paper "The Essence of the Iterator Pattern":
  * http://www.cs.ox.ac.uk/jeremy.gibbons/publications/iterator.pdf
  */
object rentalApi {
  final case class Car(id: String, model: String, make: String)
  final case class CarRental(id: String,
                             car: Car,
                             rentalFrom: LocalDateTime,
                             rentalTo: LocalDateTime,
                             fare: Fare
                            )
  final case class Fare(id: String, perHourFare: Int)

  def getRentals(from: LocalDateTime, to: LocalDateTime): List[CarRental] = {
    List(
      CarRental(
        id = "1",
        car = Car(id = "1", "CAMRY", "TOYOTA"),
        rentalFrom = from,
        rentalTo = to,
        fare = Fare(id = "1", perHourFare = 1000)
      ),
      CarRental(
        id = "2",
        car = Car(id = "2", "COROLLA", "TOYOTA"),
        rentalFrom = from,
        rentalTo = to,
        fare = Fare(id = "1", perHourFare = 1500)
      )
    )
  }

  def rentalCollection(from: LocalDateTime, to: LocalDateTime): Double = {
    getRentals(from, to).foldLeft(0l) {
      case (total, rental) =>
        total +
          ((rental.rentalTo.toEpochSecond(ZoneOffset.of("-08:00")) -
          rental.rentalFrom.toEpochSecond(ZoneOffset.of("-08:00"))) / (60 * 60)) *
        rental.fare.perHourFare
    } / 100
  }
}

object IteratorPatternClient {

  def main(args: Array[String]): Unit = {
    val rentalColl =
      rentalApi.rentalCollection(LocalDateTime.now(), LocalDateTime.now().plusDays(2))

    println(rentalColl)
  }
}
