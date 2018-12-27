package behavioural

object serviceApi {

  sealed trait State {
    def prevState: Option[State]
    def nextState: Option[State]
  }

  case class Available() extends State {
    override def prevState: Option[State] = Some(this)
    override def nextState: Option[State] = Some(Processing())
  }

  case class Processing() extends State {
    override def prevState: Option[State] = Some(Available())
    override def nextState: Option[State] = Some(Completed())
  }

  case class Completed() extends State {
    override def prevState: Option[State] = Some(Processing())
    override def nextState: Option[State] = Some(Available())
  }

  object AvailabilityStateContext {
    private var state: Option[State] = Some(Available())

    def currentState: Option[State] = state
    def updateState(): Option[State] = {
      state = state.flatMap(_.nextState)
      state
    }
  }
}

object statefulStack {

  type Stack = List[Int]
  def pop(stack: Stack): (Int, Stack) = stack match {
    case x :: xs => (x, xs)
  }

  def push(a: Int, stack: Stack): (Unit, Stack) =
    ((), a :: stack)

  def ops(pushX: Int, stack: Stack): (Int, Stack) = {
    val (_, newStack1) = push(pushX, stack)
    val (a, newStack2) = pop(newStack1)
    pop(newStack2)
  }
}

object statefulStack2 {

  import scalaz._

  type Stack = List[Int]

  val pop = State[Stack, Int] {
    case x :: xs => (xs, x)
  }

  def push(a: Int) = State[Stack, Unit] {
    case xs => (a :: xs, ())
  }

  def ops: State[Stack, Int] = for {
    _ <- push(10)
    a <- pop
    b <- pop
  } yield b
}

object StatePatternClient {

  import serviceApi._

  def main(args: Array[String]): Unit = {
    println(AvailabilityStateContext.updateState())
    println(AvailabilityStateContext.updateState())
    println(AvailabilityStateContext.updateState())
    println(AvailabilityStateContext.updateState())

    import statefulStack._
    val x = ops(10, List(1, 2, 3, 4, 5))
    println(x)

    val initialStack = List(1, 2, 3, 4, 5)
    val y = statefulStack2.ops.apply(initialStack)
    println(y)
  }

}
