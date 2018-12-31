package behavioural

import java.net.URL

// Image processing - v1 using opencv - https://docs.opencv.org/2.4/doc/tutorials/introduction/desktop_java/java_dev_intro.html
// v2 http://openimaj.org/tutorial/index.html

trait FaceDetectorService {
  def detectFaces(inputImage: URL): String
}

object faceDetectionApiV1 {

  import org.opencv.core.Core
  import org.opencv.core.Mat
  import org.opencv.core.MatOfRect
  import org.opencv.core.Point
  import org.opencv.core.Rect
  import org.opencv.core.Scalar
  import org.opencv.highgui.HighGui
  import org.opencv.objdetect.CascadeClassifier
  import org.opencv.imgcodecs.Imgcodecs
  import org.opencv.imgproc.Imgproc

  class FaceDetectionInstanceV1 extends FaceDetectorService {

    println(System.getProperty("java.library.path"))
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

    def detectFaces(inputImageUrl: URL): String = {
      val inputImage = Imgcodecs.imread(inputImageUrl.getPath)

      val classifier = new CascadeClassifier(
        getClass.getResource("data/lbpcascades/lbpcascade_frontalface.xml").getPath)
      val faceDetections = new MatOfRect()
      classifier.detectMultiScale(inputImage, faceDetections)

      // Drawing boxes
      for (rect <- faceDetections.toArray()) {
        Imgproc.rectangle(
          inputImage,
          new Point(rect.x, rect.y),
          new Point(rect.x + rect.width, rect.y + rect.height),
          new Scalar(0, 255, 0)
        )
      }

      val newImageFile = inputImageUrl.getPath + "-detect.jpg"
      Imgcodecs.imwrite(newImageFile, inputImage)

      newImageFile
    }
  }
}

object client {

  import faceDetectionApiV1._

  object ApplicationConfig {
    lazy val detectionInstance: FaceDetectorService = new FaceDetectionInstanceV1
  }
}

object StrategyPattern {

  import client.ApplicationConfig._

  def main(args: Array[String]): Unit = {
    detectionInstance.detectFaces(getClass.getResource("PorcupineTree.jpg"))
  }
}
