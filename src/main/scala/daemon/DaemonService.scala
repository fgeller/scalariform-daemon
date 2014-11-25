package daemon

import akka.actor.{ Actor, ActorRef, Props }
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scalariform.formatter.ScalaFormatter
import scalariform.formatter.preferences.{ IFormattingPreferences, PreferencesImporterExporter }
import scalax.io.{ Codec, Input, Output, Resource }
import spray.routing._

object Utils {

  def timing[T](what: String)(fun: ⇒ T) = {
    val startTime = System.nanoTime
    val result = fun
    val elapsedTime = System.nanoTime - startTime
    println(s"Finished $what in ${elapsedTime / 1000 / 1000}ms.")
    result
  }

}

case class FileFormatRequest(fileName: String, preferencesFile: String)

trait FileFormatter {

  def loadPreferences(fileName: String) = PreferencesImporterExporter.loadPreferences(fileName)
  def formatString(content: String, preferences: IFormattingPreferences) = ScalaFormatter.format(content, preferences)
  def openFile(fileName: String) = (
    Resource.fromFile(fileName),
    Resource.fromOutputStream(new java.io.FileOutputStream(fileName))
  )

  def performFormat(fileName: String, preferencesFile: String) = {
    val preferences = loadPreferences(preferencesFile)
    val (in, out) = openFile(fileName)
    val contents = in.string(Codec.UTF8)
    val formatted = formatString(contents, preferences)

    if (formatted != contents) out.write(formatted)(Codec.UTF8)
    else println(s"No need to format.")
  }

  def formatFile(fileName: String, preferencesFile: String) = Utils.timing("formatting") {
    performFormat(fileName, preferencesFile)
  }

}

class FileFormatterActor extends Actor with FileFormatter {

  def receive = {
    case FileFormatRequest(fileName, preferencesFile) ⇒ sender ! formatFile(fileName, preferencesFile)
  }

}

class DaemonServiceActor extends Actor with DaemonService {
  def actorRefFactory = context
  def receive = runRoute(daemonRoutes)
  def timeout = Timeout(10 seconds)
  val fileFormatter = context.actorOf(Props[FileFormatterActor], name = "file-formatter")
}

trait DaemonService extends HttpService {
  implicit val executionContext = actorRefFactory.dispatcher
  def timeout: Timeout
  implicit val askTimeout = timeout
  val fileFormatter: ActorRef

  def daemonRoutes = {
    pathPrefix("format") {
      get {
        parameters('fileName.as[String], 'preferencesFile.as[String]).as(FileFormatRequest) { req ⇒
          (fileFormatter ? req)
          complete { s"Received and scheduled $req" }
        }
      }
    }
  }

}
