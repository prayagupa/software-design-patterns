name := "software-patterns"

scalacOptions += "-Ypartial-unification"

private val DeeplearningVersion = "1.0.0-beta3"

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-concurrent" % "7.2.27",
  "org.scalaz" %% "scalaz-core" % "7.2.27",
  "io.monix" %% "monix" % "2.3.3",
  "org.typelevel" %% "cats-effect" % "1.1.0",
  
  "org.deeplearning4j" % "deeplearning4j-core" % DeeplearningVersion,
  "org.deeplearning4j" % "deeplearning4j-nlp" % DeeplearningVersion,
  "org.nd4j" % "nd4j-native-platform" % DeeplearningVersion,

  "org.slf4j" % "slf4j-api" % "1.8.0-beta2",
  "org.slf4j" % "slf4j-simple" % "1.8.0-beta2"
)
