import sbt._
import Keys._
import sbt.Defaults._

object ScalariformDaemonBuild extends Build {

  val buildScalaVersion = "2.10.1"

  val scalaLibrary = "org.scala-lang" % "scala-library" % buildScalaVersion
  val scalaReflect = "org.scala-lang" % "scala-reflect" % buildScalaVersion

  val akkaActor   = "com.typesafe.akka" %% "akka-actor" % "2.1.4"
  val akkaSlf4j   = "com.typesafe.akka" %% "akka-slf4j" % "2.1.4"
  val akkaTestKit = "com.typesafe.akka" %% "akka-testkit" % "2.1.4"

  val sprayCan     = "io.spray" % "spray-can"     % "1.1-20130530"
  val sprayIo      = "io.spray" % "spray-io"      % "1.1-20130530"
  val sprayRouting = "io.spray" % "spray-routing" % "1.1-20130530"

  val scalaIo = "com.github.scala-incubator.io" %% "scala-io-core" % "0.4.2"

  val scalariform = "org.scalariform" %% "scalariform" % "0.1.4"

  lazy val standardDependencies = Seq(
    libraryDependencies ++= Seq(
      scalaLibrary,
      scalaReflect,
      scalaIo,
      akkaActor,
      akkaSlf4j,
      akkaTestKit,
      scalariform,
      sprayCan,
      sprayRouting
    )
  )

  lazy val standardSettings = defaultSettings ++ standardDependencies ++ Seq(
    scalaVersion := buildScalaVersion,
    resolvers += "spray repo" at "http://nightlies.spray.io/",
    scalacOptions ++= Seq(
      "-deprecation",
      "-unchecked",
      "-feature",
      "-language:postfixOps",
      "-language:implicitConversions",
      "-language:reflectiveCalls",
      "-language:existentials",
      "-encoding",
      "utf8"
    ),
    fork in Test := true
  )

  lazy val root = Project(
    "master", file("."),
    settings = Seq(scalaVersion := buildScalaVersion) ++ standardSettings
  )

}
