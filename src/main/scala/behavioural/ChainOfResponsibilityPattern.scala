package behavioural

import java.time.LocalDateTime
import java.util.UUID

object shippingApi {

  trait Handler[I]{
    val nextStage: Handler[I]
    def process(i: I): I
  }

  final case class CustomerOrder(id: UUID,
                                 pickedUpDate: Option[LocalDateTime],
                                 packedUpDate: Option[LocalDateTime],
                                 shippedDate: Option[LocalDateTime]
                                )
  final case class PickedUpOrder(id: UUID, pickedUpDate: LocalDateTime)
  class PickupApi extends Handler[CustomerOrder] {
    val nextStage: Handler[CustomerOrder] = new PackupApi

    def process(i: CustomerOrder): CustomerOrder =
      nextStage.process(i.copy(pickedUpDate = Some(LocalDateTime.now())))
  }

  object PickupApi {
    val instance = new PickupApi
  }

  final case class PackedUpOrder(id: UUID, packedDate: LocalDateTime)
  class PackupApi extends Handler[CustomerOrder] {
    override val nextStage: Handler[CustomerOrder] = new ShippingApi

    def process(i: CustomerOrder): CustomerOrder =
      nextStage.process(i.copy(packedUpDate = Some(LocalDateTime.now())))
  }

  final case class ShippedOrder(id: UUID, shippedDate: LocalDateTime)
  class ShippingApi extends Handler[CustomerOrder] {
    override val nextStage: Handler[CustomerOrder] = null

    def process(i: CustomerOrder): CustomerOrder = i.copy(shippedDate = Some(LocalDateTime.now()))
  }
}

object ChainOfResponsibilityPatternClient {

  import shippingApi._

  def main(args: Array[String]): Unit = {
    val shipped = PickupApi.instance.process(
      CustomerOrder(
        id = UUID.randomUUID(),
        None,
        None,
        None
      )
    )

    println(shipped)
  }
}
