package com.ibm.scalalab.api

/** imports scalaxb generated package */

import com.ibm.scalalab.domain.{CustomerAccountSummaryResponse, AccountSummaryResponse}
import com.ibm.scalalab.json.AccountSummaryJsonProtocol
import com.ibm.scalaxb._

import scala.concurrent.Future
import scalaxb.{DispatchHttpClientsAsync, Soap11ClientsAsync}

/**
 * AccountSummaryInvoker object hits SOAP webservice using scalaxb to get the response and writes it to required response format.
 * Also, describes 'Future' which is an object holding value which may or may not exists.
 */
object AccountSummaryInvoker {

  def getAccountSummary : String = {
    /** this is scalaxb's async implementation of SOAP envelope case classes */
    val remote = new AccountSummarySoapBindings with Soap11ClientsAsync with DispatchHttpClientsAsync{}
    var accountStr:String = "Hello"

    /** future method starts asynchronous computation of account summary and returns a future holding response of customer's account details */
    val response:Future[Account] = remote.service.getAccountSummary(Some("mandadpe@in.ibm.com"),Some("Mandar"),Some("Dadpe"),Some("9420411799"))

    /** writes SOAP response to JSON format and return as a string  */
    response.onComplete(account=>{
      accountStr = AccountSummaryJsonProtocol.AccountSummaryResponseFormat.write(AccountSummaryResponse(account.get.accountId.get, account.get.accountType.get, account.get.balance.get, account.get.firstNames.get, account.get.lastName.get,account.get.accountNumber.get, account.get.customerId.get, account.get.phoneNumber.get, account.get.emailId.get)).toString()
      AccountSummaryService.updateCustomerAccount(Seq(CustomerAccountSummaryResponse(account.get.accountId.get.toInt,account.get.customerId.get.toInt,account.get.firstNames.get, account.get.lastName.get,account.get.accountNumber.get, account.get.accountType.get, account.get.balance.get.toDouble, account.get.phoneNumber.get, account.get.emailId.get)))
      println(accountStr)
    })

    /*   while(!response.isCompleted){
         Thread.sleep(1000)
       }*/
    accountStr
  }

}
