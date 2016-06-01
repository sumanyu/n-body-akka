package core.http

import akka.http.scaladsl.server.Directives._
import akka.util.Timeout

import scala.concurrent.duration._

trait HttpRoute extends CorsSupport {

  val timeoutPeriod = 3.seconds
  implicit val timeout = Timeout(timeoutPeriod)

  val route = pathPrefix("v2") {
    corsHandler {
      pathEndOrSingleSlash {
        get {
          complete("hello world!")
        }
      }
    }
  }
}