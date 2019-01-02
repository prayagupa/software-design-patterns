package behavioural

import java.net.URL

// Image processing - v1 using opencv - https://docs.opencv.org/2.4/doc/tutorials/introduction/desktop_java/java_dev_intro.html
// v2 http://openimaj.org/tutorial/index.html

trait FaceDetectorService {
  def detectFaces(inputImage: URL, outputImageName: String): String
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

  // install opencv3.4.5 on macos
  // brew install ant
  // brew install opencv3 --with-java --with-python3 // did not work
  // brew install --build-from-source opencv3
  // https://opencv-java-tutorials.readthedocs.io/en/latest/01-installing-opencv-for-java.html

  //  ll /usr/local/Cellar/opencv/3.4.5/share/OpenCV/java/
  //  total 4904
  //  -rwxr-xr-x  1 a1353612  admin  1800008 Dec 31 16:30 libopencv_java345.dylib
  //  -rw-r--r--  1 a1353612  admin   707562 Dec 31 16:21 opencv-345.jar
  //FIXME Exception in thread "main" java.lang.UnsatisfiedLinkError: no opencv_java320 in java.library.path

  //run
  // sbt -Djava.library.path=/usr/local/Cellar/opencv/3.4.5/share/OpenCV/java/ "runMain behavioural.StrategyPatternClient"
  class FaceDetectionInstanceV1 extends FaceDetectorService {

    private val Root = "/usr/local/Cellar/opencv/3.4.5/share/OpenCV/"
    private val FrontFaceTrainingData1 = "lbpcascades/lbpcascade_frontalface.xml"
    private val FrontFaceTrainingData2 = "haarcascades/haarcascade_frontalface_default.xml"
    private val FrontFaceTrainingData3 = "haarcascades/haarcascade_frontalface_alt.xml"
    //private val FrontFaceTrainingResource = getClass.getClassLoader.getResource(FrontFaceTrainingData).getPath

    println("[INFO] java.library.path: " + System.getProperty("java.library.path"))
    println("[INFO] FrontFaceTrainingResource: " + Root + FrontFaceTrainingData3)

    System.loadLibrary("opencv_java345")

    def detectFaces(inputImageUrl: URL, destination: String): String = {
      println("[INFO]: input image " + inputImageUrl)
      val inputImage: Mat = Imgcodecs.imread(inputImageUrl.getPath)

      val faceDetections = new MatOfRect()

      val classifier = new CascadeClassifier(Root + FrontFaceTrainingData3)
      classifier.detectMultiScale(inputImage, faceDetections)

      // Drawing boxes
      for (detectedFaceCoOrd <- faceDetections.toArray) {
        Imgproc.rectangle(
          inputImage,
          new Point(detectedFaceCoOrd.x, detectedFaceCoOrd.y),
          new Point(detectedFaceCoOrd.x + detectedFaceCoOrd.width, detectedFaceCoOrd.y + detectedFaceCoOrd.height),
          new Scalar(0, 255, 0)
        )
      }

      val newImageFile = destination
      Imgcodecs.imwrite(newImageFile, inputImage)

      newImageFile
    }
  }
}

object faceDetectionApiV2 {

  import org.openimaj.image.FImage
  import org.openimaj.image.ImageUtilities
  import org.openimaj.image.processing.face.detection.DetectedFace
  import org.openimaj.image.processing.face.detection.HaarCascadeDetector
  import org.openimaj.image.DisplayUtilities

  class FaceDetectionInstanceV2 extends FaceDetectorService {

    override def detectFaces(inputImage: URL, dest: String): String = {
      val detector = new HaarCascadeDetector()

      val file = ImageUtilities.readF(inputImage)
      val faces = detector.detectFaces(file).iterator()

      while (faces.hasNext) {
        println("Image:\n")
        val face = faces.next()
        val image1: FImage = face.getFacePatch
        val bi = ImageUtilities.createBufferedImage(image1)

        println(image1.getContentArea)
        DisplayUtilities.display(image1)
      }

      //FIXME
      dest
    }

  }

}

object client {

  import faceDetectionApiV1._
  import faceDetectionApiV2._

  object ApplicationConfig {
    lazy val detectionInstance: FaceDetectorService = new FaceDetectionInstanceV2
  }
}

object StrategyPatternClient {

  import client.ApplicationConfig._

  def main(args: Array[String]): Unit = {
    detectionInstance.detectFaces(
      getClass.getClassLoader.getResource("PorcupineTree.jpg"),
      "destination1.jpg"
    )

  }
}
