name := "software-patterns"

scalacOptions += "-Ypartial-unification"

val DeeplearningVersion = "1.0.0-beta3"
val OpenImageProcessingLib = "1.3.8"

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-concurrent" % "7.2.27",
  "org.scalaz" %% "scalaz-core" % "7.2.27",
  "io.monix" %% "monix" % "2.3.3",
  "org.typelevel" %% "cats-effect" % "1.1.0",

  "org.deeplearning4j" % "deeplearning4j-core" % DeeplearningVersion,
  "org.deeplearning4j" % "deeplearning4j-nlp" % DeeplearningVersion,
  "org.nd4j" % "nd4j-native-platform" % DeeplearningVersion,

  "org.openpnp" % "opencv" % "3.2.0-0",

  "org.slf4j" % "slf4j-api" % "1.8.0-beta2",
  "org.slf4j" % "slf4j-simple" % "1.8.0-beta2",

  "org.openimaj" % "core" % OpenImageProcessingLib,
  "org.openimaj" % "image-feature-extraction" % OpenImageProcessingLib
    exclude("com.lowagie", "itext"),
  //"org.openimaj" % "clustering" % OpenImageProcessingLib
  "org.openimaj" % "faces" % OpenImageProcessingLib % "compile",
  "org.openimaj" % "image-processing" % OpenImageProcessingLib % "compile"
    exclude("com.lowagie", "itext")
)

resolvers ++= Seq(
  "maven central" at "http://central.maven.org/maven2/",
  "ph edinburgh" at "https://www2.ph.ed.ac.uk/maven2/",
  "geo majas" at "http://maven.geomajas.org/",
  "conjars" at "http://conjars.org/repo/",
  "sonatype" at "https://oss.sonatype.org/content/repositories/releases/",
  "rh" at "https://maven.repository.redhat.com/ga/",
  "otto group" at "https://dl.bintray.com/ottogroup/maven/",
  "open-imaj" at "http://maven.openimaj.org/",
  "stuff" at "https://repository.liferay.com/nexus/content/groups/public"
)
