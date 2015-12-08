package com.ibm.scalalab.api

/** Import scalaxb generated case class account object */
import generated.Account
import spray.json.DefaultJsonProtocol

/**
  * @define : case class for getAccountSummary request method
  * @param : customer's account number, first name and age
  */
case class AccountSummary(accountNumber: String, firstName: String, age: Int)

/**
  * @define : case class for getAccountSummary request method
  * @param : customer's account ID, account type and balance amount
  */
case class AccountSummaryResponse(accountId: String, accountType: String, balance: String)

/**
 * @usecase : converts received account summary response from SOAP webservice to JSON format
 * @author : Mandar Dadpe
 */
object AccountSummaryJsonProtocol extends DefaultJsonProtocol {
  implicit val AccountSummaryFormat = jsonFormat3(AccountSummary)
  implicit val AccountFormat = jsonFormat3(Account)
  implicit val AccountSummaryResponseFormat = jsonFormat3(AccountSummaryResponse)
}
