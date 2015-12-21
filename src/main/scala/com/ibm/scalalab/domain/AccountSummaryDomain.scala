package com.ibm.scalalab.domain


/**
 * Created by Nishant on 12/18/2015.
 */
class AccountSummaryDomain {

}
case class Customer(id:Int,fname:String,lname:String,mobnumber:String,email:String)
case class CustomerList(customers: Seq[Customer])

case class Accounts(id:Int,anumber:String,atype:String,creationdate:String)

case class AccountList(accounts: Seq[Accounts])

case class CustomerAccount(id:Int,cid:Int,aid:Int,balance:Double)

case class CustomerAccountSummaryResponse(aid:Int,cid:Int,fname:String,lname:String,anumber:String,atype:String,balance:Double,email:String,mob:String)

case class AccountSummaryResponse(accountId: String, accountType: String, balance: String, firstName: String, lastName: String,anumber:String ,customerId: String, mobileNumber: String, emailId: String)