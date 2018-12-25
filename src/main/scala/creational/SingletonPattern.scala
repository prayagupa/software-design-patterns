package creational

final case class Item(sku: String, qty: Int, unitPrice: Int)
final case class CustomerOrder(id: String, items: List[Item])

object CustomerOrderPriorityService {

  def priority(order: CustomerOrder): Int = {
    val totalCost =
      order.items.foldLeft(0) { case (a, b) => a + ((b.qty * b.unitPrice) / 100) }

    if (totalCost > 1000) 10
    else if (totalCost > 750) 8
    else if (totalCost > 500) 5
    else if (totalCost > 250) 3
    else 1
  }
}

object SingletonPatternClient {

  def main(args: Array[String]): Unit = {
    val priority = CustomerOrderPriorityService.priority(CustomerOrder(
      id = "12",
      items = List(
        Item(
          sku = "sku1",
          qty = 2,
          unitPrice = 500 * 100
        )
      )
    ))

    println(priority)
  }
}
