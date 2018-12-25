package structural

import java.time.ZonedDateTime
import java.util.UUID

object customerApi {

  final case class Customer(id: UUID, name: String, created: ZonedDateTime)

  trait CustomerService {
    def createCustomer(customer: Customer): Customer
    def getCustomer(customerId: UUID): Customer
  }

  final class CustomerServiceV1 extends CustomerService {
    def createCustomer(customer: Customer): Customer = customer

    def getCustomer(customerId: UUID): Customer = Customer(
      id = customerId,
      name = "upd",
      created = ZonedDateTime.now()
    )
  }

  object CustomerServiceV1 {
    lazy val instance = new CustomerServiceV1
  }
}

object orderApi {

  final case class Order(id: String, customerId: UUID, items: List[Item])
  final case class Item(skuId: String, qty: Int, unitPrice: Int)

  trait OrderService {
    def createOrder(order: Order): Order
    def getOrders(customerId: UUID): List[Order]
  }

  final class OrderServiceV1 extends OrderService {
    def createOrder(order: Order): Order = order

    def getOrders(customerId: UUID): List[Order] = List(
      Order(
        id = "order-1",
        customerId = customerId,
        items = List(
          Item(
            skuId = "123",
            qty = 1,
            unitPrice = 1000
          )
        )
      )
    )
  }

  object OrderServiceV1 {
    lazy val instance = new OrderServiceV1
  }
}

object gatewayApi {
  import customerApi._
  import orderApi._

  trait GatewayService {
    type A <: CustomerService
    type B <: OrderService

    protected val customerService: A
    protected val orderService: B

    def getOrders(customerId: UUID): List[Order] = orderService.getOrders(customerId)
    def getCustomer(id: UUID): Customer = customerService.getCustomer(id)
  }

  object GatewayService extends GatewayService {
    type A = CustomerService
    type B = OrderService
    protected val customerService: CustomerServiceV1 = CustomerServiceV1.instance
    protected val orderService: OrderServiceV1 = OrderServiceV1.instance
  }

}

object FacadePatternClient {

  import gatewayApi._

  def main(args: Array[String]): Unit = {
    val customer = GatewayService.getCustomer(UUID.randomUUID())
    val orders = GatewayService.getOrders(customer.id)
  }
}
