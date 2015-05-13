organization := "fgeller"

name := "scalariform-deamon"

version :="0.1.0-SNAPSHOT"

scalaVersion := "2.11.6"

libraryDependencies ++= {
  val akkaVersion = "1.0-RC2"
  Seq(
    "com.typesafe.akka" %% "akka-http-scala-experimental" % akkaVersion,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaVersion,
    "com.github.scala-incubator.io" %% "scala-io-core" % "0.4.3-1",
    "org.scalariform" %% "scalariform" % "0.1.6"
  )
}

assemblyJarName in assembly := "scalariform-daemon.jar"
