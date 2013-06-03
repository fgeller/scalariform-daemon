package daemon

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http


object Runner extends App {

  implicit val system = ActorSystem("scalariform-daemon")

  val handler = system.actorOf(Props[DaemonServiceActor], name = "handler")

  IO(Http) ! Http.Bind(handler, "localhost", port = 8080)

}
