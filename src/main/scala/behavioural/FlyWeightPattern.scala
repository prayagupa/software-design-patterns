package behavioural

/**
  * Use sharing to support large numbers of fine-grained objects efficiently.
  */

object musicApi {

  case class IntrinsicState(name: String,
                            color: String,
                            brand: String,
                            image: String,
                            cost: Double)

  trait Instrument {
    val info: IntrinsicState
  }

  final case class Guitar(info: IntrinsicState) extends Instrument

  final case class Drum(info: IntrinsicState,
                        rideCymbal: Int,
                        splashCymbal: Int,
                        chinaCymbal: Int,
                        crashCymbal: Int) extends Instrument

  private val cacheWith10_5Instruments = Map[(String, String), List[Instrument]](
    ("guitar", "fender") -> List(
      Guitar(
        info = IntrinsicState("guitar",
          "black",
          "fender",
          "image",
          1000 * 100d
        ))),
    ("drumset", "z") -> List(
      Drum(
        info = IntrinsicState(
          "drum",
          "black",
          "fender",
          "image",
          2000 * 100d
        ),
        rideCymbal = 1,
        splashCymbal = 1,
        chinaCymbal = 1,
        crashCymbal = 1,
      ))
  )

  def getInstrument(instrumentType: String, brand: String): List[Instrument] = {
    cacheWith10_5Instruments.get((instrumentType, brand)) match {
      case Some(ins) => ins
      case None => List.empty[Instrument]
    }
  }

}

object FlyWeightPatternClient {

  import musicApi._

  def main(args: Array[String]): Unit = {
    val instruments = getInstrument("guitar", "fender")
    println(instruments)
  }

}
