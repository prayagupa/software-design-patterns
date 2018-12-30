package behavioural

import java.time.LocalDateTime

final case class CustomerSupportRequest(problem: String,
                                        location: String,
                                        phoneNumber: String,
                                        requestedDate: LocalDateTime)

object publisherApi {

  import monix.reactive._
  import concurrent.duration._

  val requestStream: Observable[CustomerSupportRequest] = Observable.repeat {
    CustomerSupportRequest(
      problem = "lights do not turn on",
      location = "first hill",
      phoneNumber = "111-111-1111",
      requestedDate = LocalDateTime.now()
    )
  }

  import cats.effect._
  import cats.effect.concurrent._
  import cats.syntax.all._
  import cats.effect.concurrent.MVar

  //eq: https://docs.oracle.com/javase/10/docs/api/java/util/concurrent/BlockingQueue.html
  //https://typelevel.org/cats-effect/concurrency/mvar.html
  type Stream[A] = MVar[IO, Option[A]]

  def requestStreamV2IO(stream: Stream[Int], list: List[Int]): IO[Unit] =
    list match {
      case Nil =>
        stream.put(None)
      case head :: tail =>
        stream.put(Some(head)).flatMap(_ => requestStreamV2IO(stream, tail))
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

  import publisherApi.Stream
  import cats.effect._
  import cats.effect.concurrent._
  import cats.syntax.all._

  def requestSubscriberV2IO(stream: Stream[Int], sum: Long): IO[Long] =
    stream.take.flatMap {
      case Some(x) =>
        requestSubscriberV2IO(stream, sum + x)
      case None =>
        IO.pure(sum)
    }
}

object ObservablePatternClient {

  def main(args: Array[String]): Unit = {

    import publisherApi._
    import consumerApi._

    val program = requestStream.subscribe(requestSubscriber)

    import cats.effect.concurrent.MVar
    import cats.effect._
    import cats.effect.concurrent._
    import cats.syntax.all._

    //ContextShift required for
    // 1) MVar.apply
    // 2) IO.start
    import scala.concurrent.ExecutionContext
    implicit val cs = IO.contextShift(ExecutionContext.Implicits.global)

    val programV2 = for {
      stream <- MVar[IO].empty[Option[Int]]
      count = 100000
      producerTask = requestStreamV2IO(stream, (0 until count).toList)
      subscriptionTask = requestSubscriberV2IO(stream, 0L)

      producerFiber  <- producerTask.start(cs)
      subscriptionFiber  <- subscriptionTask.start(cs)
      _   <- producerFiber.join
      sum <- subscriptionFiber.join
    } yield sum

    val sum = programV2.unsafeRunSync()
    println("sum: " + sum)
  }
}
