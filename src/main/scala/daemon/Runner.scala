package daemon

import akka.actor.ActorSystem 
import akka.stream.ActorFlowMaterializer
import akka.http.scaladsl.Http

object Runner extends App with DaemonService {
  override implicit val system = ActorSystem()
  override implicit val executor = system.dispatcher
  override implicit val materializer = ActorFlowMaterializer()

  Http().bindAndHandle(routes, "localhost", 5474)
}
