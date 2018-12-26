package behavioural

object api {

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
object StatePatternClient {

  import api._

  def main(args: Array[String]): Unit = {
     println(AvailabilityStateContext.updateState())
      println(AvailabilityStateContext.updateState())
      println(AvailabilityStateContext.updateState())
      println(AvailabilityStateContext.updateState())
  }

}
