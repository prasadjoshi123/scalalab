package com.ibm.scalalab.api

/** Import scalaxb generated case class account object */

import java.sql.Timestamp
import com.ibm.scalalab.domain.{CustomerList, AccountList, AccountSummaryResponse}
import spray.json._


/**
  * @define : case class to hold and represent response fetched from the SOAP webservice
  * param : customer's account ID, account type and balance amount
  */


/**
 * @usecase : converts received account summary response from SOAP to JSON format
 * @author : Mandar Dadpe
 */
//object AccountSummaryJsonProtocol extends DefaultJsonProtocol {
//  implicit val AccountSummaryResponseFormat = jsonFormat8(AccountSummaryResponse)
//  implicit object TimestampFormat extends JsonFormat[Timestamp] {
//    def write(obj: Timestamp) = JsNumber(obj.getTime)
//
//    def read(json: JsValue) = json match {
//      case JsNumber(time) => new Timestamp(time.toLong)
//
//      case _ => throw new DeserializationException("Date expected")
//    }
//  }
//}
//
//object AccountListProtocol extends DefaultJsonProtocol {
//  implicit val AccountListFormat = jsonFormat1(AccountList.apply)
//}
//
//object CustomerListProtocol extends DefaultJsonProtocol {
//  implicit val CustomerListFormat = jsonFormat1(CustomerList.apply)
//}