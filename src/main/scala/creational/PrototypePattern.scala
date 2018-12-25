package creational

final case class HotelBooking(id: String,
                              customer: String,
                              roomNumber: String,
                              priceInCents: Int)

object BookingPatternClient {

  def main(args: Array[String]): Unit = {
    val hotelBooking = HotelBooking(
      id = "1234",
      customer = "Stephen Hawking",
      roomNumber = "A1",
      priceInCents = 10000
    )

    //scala provides in built copy to clone object state
    val prototypeBooking = hotelBooking.copy()
    println(prototypeBooking)

    //equality on case classes compares the state
    //not reference
    val equality = hotelBooking == prototypeBooking
    println(equality)
  }
}
