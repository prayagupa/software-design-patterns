package behavioural

import org.slf4j.{Logger, LoggerFactory}

/**
  * https://nlp.stanford.edu/projects/glove/
  * https://github.com/deeplearning4j/dl4j-examples/blob/master/dl4j-examples/src/main/java/org/deeplearning4j/examples/nlp/glove/GloVeExample.java
  *
  * decorator:
  * “Attach additional responsibilities to an object dynamically.
  * Decorators provide a flexible alternative to subclassing for extending functionality.
  */
object languageApi {

  trait NearestWords {
    def findNearestWords(documentName: String, word: String): List[String]
  }

  import org.nd4j.linalg.io.ClassPathResource
  import org.deeplearning4j.text.sentenceiterator.BasicLineIterator
  import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor
  import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory
  import org.deeplearning4j.models.glove.Glove
  import scala.collection.JavaConverters._

  class NearestWordsImpl extends NearestWords {

    val logger = LoggerFactory.getLogger(classOf[NearestWordsImpl])

    def findNearestWords(fileName: String, word1: String): List[String] = {
      val document = new ClassPathResource(fileName).getFile
      println(s"finding nearest words in $document")

      val tokenizer = new DefaultTokenizerFactory()
      tokenizer.setTokenPreProcessor(new CommonPreprocessor())

      val GLObalVEctor = new Glove.Builder()
        .iterate(new BasicLineIterator(document.getAbsolutePath))
        .tokenizerFactory(tokenizer)
        .alpha(0.75)
        .learningRate(0.1)
        .epochs(1) // number of epochs for training
        .xMax(100) // cutoff for weighting function
        .batchSize(100) // training is done in batches taken from training corpus
        .shuffle(true) // if set to true, batches will be shuffled before training
        .symmetric(true) // if set to true word pairs will be built in both directions, LTR and RTL
        .build()

      println(s"start training for finding nearest words") //FIXME never completes
      GLObalVEctor.fit()
      //val simD = GLObalVEctor.similarity(word1, word2)
      val words = GLObalVEctor.wordsNearest(word1, 10)
      words.asScala.toList
    }
  }


}

object DecoratorPatternClient {

  import languageApi._

  class NearestWordsExtension(nearestWordsInstance: NearestWords) {

    def findNearestWords(fileName: String, word: String): List[String] = {
      println("some stuff")
      nearestWordsInstance.findNearestWords(fileName, word)
    }
  }

  object NearestWordsExtension {
    val instance = new NearestWordsExtension(new NearestWordsImpl)
  }

  def main(args: Array[String]): Unit = {

    val words = NearestWordsExtension.instance.findNearestWords(
      "raw_sentences.txt",
      "universe"
    )

    println(words)
  }
}
