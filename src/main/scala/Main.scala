package test

import akka.actor.Actor
import akka.actor.ActorRefFactory
import akka.actor.Props
import akka.dispatch.ExecutionContext
import akka.dispatch.Future
import spray.can.server.SprayCanHttpServerApp
import spray.http.HttpRequest
import spray.http.MediaTypes._
import spray.http.StatusCodes._
import spray.httpx.encoding._
import spray.httpx.encoding.{Deflate,Gzip,NoEncoding}
import spray.json._
import spray.json.DefaultJsonProtocol._
import spray.httpx.SprayJsonSupport._
import spray.routing.HttpService
import spray.routing.RequestContext
import spray.routing.authentication.BasicAuth
import spray.routing.authentication.BasicUserContext
import spray.routing.authentication.UserPass
import spray.routing.authentication.UserPassAuthenticator
import spray.routing.Directives._
import spray.routing.directives.CachingDirectives._
import spray.routing.authentication.BasicHttpAuthenticator
import spray.routing.HttpService
import spray.util.LoggingContext


case class MyRequest(test: String)

object Main extends SprayCanHttpServerApp {
	def main (args: Array[String]): Unit = {
        val service = system.actorOf(Props[RestServiceActor], "RestService")
		newHttpServer(service) ! Bind(interface = "0.0.0.0", port = 5555)
	}
}

class RestServiceActor extends Actor with RestService {
	def actorRefFactory: ActorRefFactory = context
	def receive = runRoute(route)
}

class MyAuth (implicit ec: ExecutionContext, logging: LoggingContext) extends UserPassAuthenticator[Unit] {
	def apply (credentials: Option[UserPass]) = Future { if (credentials.isDefined) Some(Unit) else None }
}

trait RestService extends HttpService {

	implicit val requestFormat = jsonFormat1(MyRequest)

	def realm = BasicAuth[Unit](new MyAuth, "test")

	val route = { 
		pathPrefix("v1") {
			path("test") {
				authenticate(realm) { user =>
					post {
						(decodeRequest(Gzip) | decodeRequest(Deflate) | decodeRequest(NoEncoding)) {
							entity(as[MyRequest]) { request =>
								complete {
									request
								}
							}
						}
					}
				}
			}
		}
	}
}
