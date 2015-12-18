package com.ibm.scalalab.json

import java.sql.Timestamp
import com.ibm.scalalab.domain._
import spray.json
import spray.json._
import spray.can.Http
import spray.http.HttpHeaders.RawHeader
import spray.http._
import spray.httpx.marshalling.{ToResponseMarshaller, Marshaller}
import spray.json._
import spray.routing._
import spray.routing.directives.{RouteDirectives}
import spray.util.LoggingContext
import spray.http.StatusCodes._
import scala.concurrent.{Await, Future, ExecutionContext}
import util.control.NonFatal
import spray.http.HttpMethods.GET
import scala.concurrent.ExecutionContext.Implicits.global
/**
 * Created by Nishant on 12/18/2015.
 */
object AccountSummaryJsonProtocol extends DefaultJsonProtocol {
  implicit val AccountSummaryResponseFormat = jsonFormat8(AccountSummaryResponse)
  implicit val CustomerResponseFormat = jsonFormat5(Customer)
  implicit val AccountsFormat = jsonFormat4(Accounts)
  implicit object TimestampFormat extends JsonFormat[Timestamp] {
    def write(obj: Timestamp) = JsNumber(obj.getTime)

    def read(json: JsValue) = json match {
      case JsNumber(time) => new Timestamp(time.toLong)

      case _ => throw new DeserializationException("Date expected")
    }
  }
}

object AccountListProtocol extends DefaultJsonProtocol {
  import com.ibm.scalalab.json.AccountSummaryJsonProtocol._
  implicit val AccountListFormat = jsonFormat1(AccountList.apply)
}

object CustomerListProtocol extends DefaultJsonProtocol {
  import com.ibm.scalalab.json.AccountSummaryJsonProtocol._
  implicit val CustomerListFormat = jsonFormat1(CustomerList.apply)
}
//object MyJsonProtocol extends DefaultJsonProtocol {
//  implicit val AccountSummaryFormat = jsonFormat3(AccountSummary)
//  implicit val ValidAccountSummaryFormat = jsonFormat1(ValidAccountSummary)
//  implicit val AccountFormat = jsonFormat3(generated.Account)
//  implicit val AccountSummaryResponseFormat = jsonFormat3(AccountSummaryResponse)
//  implicit val AccountResponseFormat = jsonFormat3(generated.Account)
//  implicit val CustomerResponseFormat = jsonFormat5(Customer)
//  implicit val AccountsFormat = jsonFormat4(Accounts)
//
//}