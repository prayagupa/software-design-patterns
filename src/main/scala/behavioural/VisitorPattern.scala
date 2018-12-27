package behavioural

import java.time.LocalDateTime

object eventStreamApi {
  import java.time.LocalDateTime

  sealed abstract class Event

  final case class Item(sku: String, qty: Int, unitPrice: Int)

  final case class OrderReceived(id: String,
                                 items: List[Item],
                                 createdDate: LocalDateTime
                                ) extends Event

  final case class OrderShipped(id: String,
                                items: List[Item],
                                shippedDate: LocalDateTime
                               ) extends Event

  final case class OrderCanceled(id: String,
                                 items: List[Item],
                                 canceledDate: LocalDateTime
                                ) extends Event

  final case class Processsed() extends Event
  final case class ProccessFailed(error: Throwable) extends Event

  class Db {
    def execute(ops: String): Either[Throwable, String] = Right("SUCCESS")
  }

  class ApplicationEventProcessor(db: Db) {
    def handleEvent(event: Event): Event = {
      event match {
        case or: OrderReceived =>
          toResult(
            db.execute(s"INSERT INTO orders(id, items, createdDate) VALUES (${or.id}, ${or.items}, ${or.createdDate})"))
        case os: OrderShipped =>
          toResult(
            db.execute(s"UPDATE orders SET shippedDate = ${os.shippedDate})"))
        case oc: OrderCanceled =>
          toResult(
            db.execute(s"UPDATE orders SET canceledDate = ${oc.canceledDate})"))
      }
    }

    private def toResult(res: Either[Throwable, String]) = res match {
      case Right(r) => Processsed()
      case Left(l) => ProccessFailed(l)
    }
  }

  object ApplicationConfig {
    lazy val db = new Db()
    lazy val applicationEventProcessor = new ApplicationEventProcessor(db)
  }
}

object VisitorPatternClient {

  import eventStreamApi._
  import ApplicationConfig._

  def main(args: Array[String]): Unit = {
    val a = applicationEventProcessor.handleEvent(
      OrderReceived(
        id = "123",
        items = List.empty,
        createdDate = LocalDateTime.now()
      )
    )

    println(a)
  }
}
