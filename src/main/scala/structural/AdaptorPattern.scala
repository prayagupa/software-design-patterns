package structural

import java.time.LocalDate

case class BookingRequest(id: String, customerName: String, requestedDate: LocalDate)
case class BookingConfirmation(id: String, customerName: String, bookedItem: String, bookedForDate: LocalDate)
case class BookingCanceled(id: String, customerName: String, bookedItem: String, bookedForDate: LocalDate)

object hotelApi {
  trait HotelBookingService {
    def book(bookingRequest: BookingRequest): BookingConfirmation
    def cancel(bookingRequest: BookingRequest): BookingCanceled
  }
}

object flightApiV1 {
  
  class FlightBookingService {
    def book(bookingRequest: BookingRequest): BookingConfirmation = {
      BookingConfirmation(
        id = bookingRequest.id,
        customerName = bookingRequest.customerName,
        bookedItem = "Flight ABC1",
        bookedForDate = bookingRequest.requestedDate
      )
    }
  }
}

object flightApiV2 {
  import flightApiV1._
  
  trait FlightAdaptor {
    self: hotelApi.HotelBookingService with FlightBookingService =>

    def cancel(bookingRequest: BookingRequest): BookingCanceled = BookingCanceled(
      id = bookingRequest.id,
      customerName = bookingRequest.customerName,
      bookedItem = "ABC1",
      bookedForDate = LocalDate.now()
    )
  }
}

object AdaptorPatternClient {

  import hotelApi._
  import flightApiV1._
  import flightApiV2._

  def main(args: Array[String]): Unit = {
    val flightBooking = new FlightBookingService with FlightAdaptor with HotelBookingService

    flightBooking.cancel(
      BookingRequest(
        id = "",
        customerName = "",
        requestedDate = LocalDate.now()
      )
    )
  }

}
