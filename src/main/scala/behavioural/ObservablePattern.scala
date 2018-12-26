package behavioural

object ObservablePatternClient {

  def main(args: Array[String]): Unit = {
    // We need a Scheduler in scope in order to make
    // the Observable produce elements when subscribed
    import monix.execution.Scheduler.Implicits.global
    import monix.reactive._

    import concurrent.duration._

    // We first build an observable that emits a tick per second,
    // the series of elements being an auto-incremented long
    // Filtering out odd numbers, making it emit every 2 seconds
    // We then make it emit the same element twice
    // This stream would be infinite, so we limit it to 10 items
    val publisher = Observable.interval(1.second)
      .filter(_ % 2 == 0)
      .flatMap(x => Observable(x, x))
      .take(20)

    // Observables are lazy, nothing happens until you subscribe...
    // On consuming it, we want to dump the contents to stdout
    // for debugging purposes
    // Finally, start consuming it
    val cancelable = publisher
      .dump("O")
      .subscribe()
  }
}
