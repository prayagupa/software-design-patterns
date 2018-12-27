package behavioural

final case class CustomerSupportRequest(problem: String,
                                        location: String,
                                        phoneNumber: String)

object publisherApi {

  import monix.reactive._
  import concurrent.duration._

  val requestStream: Observable[CustomerSupportRequest] = Observable.repeat {
    CustomerSupportRequest(
      problem = "lights do not turn on",
      location = "first hill",
      phoneNumber = "111-111-1111"
    )
  }
}

object consumerApi {
  import monix.execution.{Ack, Scheduler}
  import monix.reactive.observers.Subscriber
  import scala.concurrent.Future

  val requestSubscriber = new Subscriber[CustomerSupportRequest] {
    implicit def scheduler: Scheduler = monix.execution.Scheduler.Implicits.global

    def onNext(request: CustomerSupportRequest): Future[Ack] = {
      println("helping with : " + request)
      Ack.Continue
    }

    def onError(ex: Throwable): Unit = println(s"error: $ex")

    def onComplete(): Unit = println("complete")
  }
}

object ObservablePatternClient {

  def main(args: Array[String]): Unit = {

    val program = publisherApi.requestStream.subscribe(consumerApi.requestSubscriber)
  }
}
