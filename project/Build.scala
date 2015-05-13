import sbt._
import Keys._
import sbt.Defaults._

object ScalariformDaemonBuild extends Build {

  val buildScalaVersion = "2.11.6"
  val akkaVersion = "1.0-RC2"

  val akkaHttp = "com.typesafe.akka" %% "akka-http-scala-experimental" % akkaVersion
  val akkaHttpJson = "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaVersion
  val scalaIo = "com.github.scala-incubator.io" %% "scala-io-core" % "0.4.3-1"
  val scalariform = "org.scalariform" %% "scalariform" % "0.1.6"

  lazy val standardDependencies = Seq(
    libraryDependencies ++= Seq(
      scalaIo,
      scalariform,
      akkaHttp,
      akkaHttpJson
    )
  )

  lazy val standardSettings = defaultSettings ++ standardDependencies ++ Seq(
    scalaVersion := buildScalaVersion,
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
    artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
      "scalariform-daemon.jar"
    }
  )

  lazy val root = Project(
    "master", file("."),
    settings = Seq(scalaVersion := buildScalaVersion) ++ standardSettings
  )

}
