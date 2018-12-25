package structural

import java.time.LocalDateTime
import java.util.UUID

object flightApi {
  final case class Flight(id: String,
                          airlines: String,
                          fare: Int,
                          departure: LocalDateTime,
                          arrival: LocalDateTime
                         )
  trait FlightService {
    def getFlights(from: String, to: String): List[Flight]
  }

  final object FlightServiceV1 extends FlightService {
    def getFlights(from: String, to: String): List[Flight] = List(
      Flight(
        id = UUID.randomUUID().toString,
        airlines = "DELTA",
        fare = 1000,
        departure = LocalDateTime.now(),
        arrival = LocalDateTime.now()
      )
    )
  }
}

object flightProxyApi {

  import flightApi._
  import scala.concurrent.Future
  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.duration._
  import scalaz.concurrent.Task

  final object FlightPtoxyService extends FlightService {

    private var cache = Map[(String, String), List[Flight]]()

    def getFlights(from: String, to: String): List[flightApi.Flight] =
      if(cache.contains((from, to))) {
        cache((from, to))
      } else {
        val flights = FlightServiceV1.getFlights(from, to)
        Future {
          cache += (from, to) -> flights
        }
        flights
      }

    Task.schedule({
      println(s"before: $cache")
      println("updating flights cache")
      cache.keys.foreach { case (from, to) =>
        cache += (from, to) -> FlightServiceV1.getFlights(from, to)
      }
      println(s"after: $cache")
    }, 5.seconds).unsafePerformAsync { _ => }
  }
}

object ProxyPatternClient {

  import flightProxyApi._
  def main(args: Array[String]): Unit = {
    val flights = FlightPtoxyService.getFlights("SEATTLE", "SAN DIEGO")
    println(flights)

    Thread.sleep(20000)
  }
}
