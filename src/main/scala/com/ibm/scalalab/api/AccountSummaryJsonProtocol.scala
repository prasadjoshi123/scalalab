package com.ibm.scalalab.api

/** Import scalaxb generated case class account object */
import spray.json.DefaultJsonProtocol


/**
  * @define : case class to hold and represent response fetched from the SOAP webservice
  * @param : customer's account ID, account type and balance amount
  */
case class AccountSummaryResponse(accountId: String, accountType: String, balance: String, firstName: String, lastName: String, customerId: String, mobileNumber: String, emailId: String)

/**
 * @usecase : converts received account summary response from SOAP to JSON format
 * @author : Mandar Dadpe
 */
object AccountSummaryJsonProtocol extends DefaultJsonProtocol {
  implicit val AccountSummaryResponseFormat = jsonFormat8(AccountSummaryResponse)
}
