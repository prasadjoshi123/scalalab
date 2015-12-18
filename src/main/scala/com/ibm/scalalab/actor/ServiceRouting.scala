package com.ibm.scalalab.actor

import akka.actor.{Actor, ActorLogging}
import com.ibm.scalalab.api.AccountSummaryInvoker
import spray.http.StatusCodes._
import spray.http.{HttpEntity, StatusCode, HttpResponse, MediaTypes}
import spray.routing._
import spray.util.LoggingContext

import scala.concurrent.ExecutionContext
import scala.util.control.NonFatal

/**
 * This class defines the routing path for the REST url.
 * Spray will marshall object to JSON, where we set the correct response media type.
 */
class ServiceRouting(implicit executionContext: ExecutionContext) extends Directives {

  /** handles the api path to get customer's account details */
  val route = path("getAccountSummary") {
    get {
      respondWithMediaType(MediaTypes.`application/json`) {
        complete {
          HttpResponse(entity = AccountSummaryInvoker.getAccountSummary)
        }
      }
    }
  }
}
case class ErrorResponseException(responseStatus: StatusCode, response: Option[HttpEntity]) extends Exception

/** simple actor to handle the routes */
class AccountHttpServiceActor (route: Route) extends Actor with HttpService with ActorLogging {

  /** required as implicit value for the HttpService */
  implicit def actorRefFactory = context

  /** exception handler to handle inappropriate responses*/
  implicit val handler = ExceptionHandler {
    case NonFatal(ErrorResponseException(statusCode, entity)) => ctx =>
      ctx.complete(statusCode, entity)

    case NonFatal(e) => ctx => {
      log.error(e, InternalServerError.defaultMessage)
      ctx.complete(InternalServerError)
    }
  }

  /** we don't create a receive function ourselves, but use the runRoute function from HttpService to create routes */
  def receive: Receive = {
    runRoute(route) (handler, RejectionHandler.Default, context, RoutingSettings.default, LoggingContext.fromActorRefFactory)
  }
}