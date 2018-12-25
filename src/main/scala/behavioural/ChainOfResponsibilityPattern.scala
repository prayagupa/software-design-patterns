package behavioural

import java.time.{LocalDate, LocalDateTime}
import java.util.UUID

object shippingApi {

  trait Handler[I, O]{
    def process(i: I): O
  }

  final case class PickupRequest(id: UUID)
  final case class PickedUpOrder(id: UUID, pickedUpDate: LocalDateTime)
  object PickupApi extends Handler[PickupRequest, PickedUpOrder] {
    def process(i: PickupRequest): PickedUpOrder = PickedUpOrder(
      i.id,
      pickedUpDate = LocalDateTime.now()
    )
  }

  final case class PackedUpOrder(id: UUID, packedDate: LocalDateTime)
  object PackupApi extends Handler[PickedUpOrder, PackedUpOrder] {
    def process(i: PickedUpOrder): PackedUpOrder = PackedUpOrder(
      id = i.id,
      packedDate = LocalDateTime.now()
    )
  }

  final case class ShippedOrder(id: UUID, shippedDate: LocalDateTime)
  object ShippingApi extends Handler[PackedUpOrder, ShippedOrder] {
    def process(i: PackedUpOrder): ShippedOrder = ShippedOrder(
      id = i.id,
      shippedDate = LocalDateTime.now()
    )
  }
}

object ChainOfResponsibilityPatternClient {

  import shippingApi._

  def main(args: Array[String]): Unit = {
    val shipped = ShippingApi.process(
      PackupApi.process(
        PickupApi.process(PickupRequest(UUID.randomUUID()))
      )
    )
    
    println(shipped)
  }
}
