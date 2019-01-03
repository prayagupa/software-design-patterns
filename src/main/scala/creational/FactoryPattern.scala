package creational

import java.time.LocalDateTime

object lodgeService {

  import java.time.LocalDateTime

  trait LodgeService {
    def availability(date: LocalDateTime): List[String]
  }

  class LodgeServiceV1 extends LodgeService {
    def availability(date: LocalDateTime): List[String] = List("lodge1")
  }

  object LodgeServiceV1 {
    lazy val instance = new LodgeServiceV1
  }

  class LodgeServiceV2 extends LodgeService {
    def availability(date: LocalDateTime): List[String] = List("lodge2")
  }

  object LodgeServiceV2 {
    lazy val instance = new LodgeServiceV2
  }

  object LodgeService {

    def instance(clientVersion: String): LodgeService = {
      clientVersion match {
        case "v1" => LodgeServiceV1.instance
        case "v2" => LodgeServiceV2.instance
      }
    }
  }

}

object AbstractFactoryPatternClient {

  import lodgeService._

  def main(args: Array[String]): Unit = {
    val availableLodges =
      LodgeService.instance("v1").availability(LocalDateTime.now())

    println(availableLodges)
  }

}
