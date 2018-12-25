package behavioural

object api {

  case class Content(fileName: String, content: String)

  class Transaction(var contentFinalState: Content) {
    def commit(buffer: Buffer): Content = {
      val b = buffer.persist
      contentFinalState = Content(b.fileName, b.content)
      contentFinalState
    }

    def rollback(buffer: Buffer): Content = {
      buffer.rollback(contentFinalState)
    }
  }

  class Buffer(private var fileName: String,
               private var content: StringBuilder = new StringBuilder) {
    def write(data: String) = this.content.append(data)
    def persist: Content = Content(this.fileName, this.content.toString())
    def rollback(memento: Content): Content = {
      this.fileName = memento.fileName
      this.content = new StringBuilder(memento.content)
      memento
    }

    override def toString: String = {
      "buffer content: " + content.toString()
    }
  }
}

object MementoPattern {

  import api._

  def main(args: Array[String]): Unit = {

    val transaction = new Transaction(null)
    val buffer = new Buffer("data.txt")

    //1
    buffer.write("this is first data\n")
    println(buffer)
//    println("1: " + transaction.contentFinalState)
    transaction.commit(buffer)

    //2
    buffer.write("this is another data\n")
    println(buffer)
//    println("2: " + transaction.contentFinalState)

    transaction.rollback(buffer)
    println(buffer)
//    println("3: " + transaction.contentFinalState)
  }

}
