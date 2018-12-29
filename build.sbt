name := "software-patterns"

scalacOptions += "-Ypartial-unification"

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-concurrent" % "7.2.27",
  "org.scalaz" %% "scalaz-core" % "7.2.27",
  "io.monix" %% "monix" % "2.3.3",
  "org.typelevel" %% "cats-effect" % "1.1.0"
)
