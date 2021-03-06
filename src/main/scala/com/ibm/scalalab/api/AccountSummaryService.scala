package com.ibm.scalalab.api

import com.ibm.scalalab.dao.AccountSummaryDAO.{AccountTable, CustomerAccountTable, CustomerTable}
import com.ibm.scalalab.domain.{Accounts, Customer, CustomerAccount, CustomerAccountSummaryResponse}
import slick.dbio.DBIO
import slick.dbio.Effect.Write
import slick.driver.MySQLDriver.api._
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.meta.MTable
import slick.lifted.TableQuery
import slick.profile.FixedSqlAction

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/**
 * Created by Nishant on 12/18/2015.
 */
object AccountSummaryServiceTest extends App{
  AccountSummaryService.createTables
  AccountSummaryService.updateCustomerAccount(Seq(CustomerAccountSummaryResponse(5,5,"Prasad","Joshi","92212121", "SAVING", 5639, "prasad.joshi@in.ibm.com", "9432323923")))
  println("Going to sleep")
  Thread.sleep(60000)
  println("Wake up!!!")
  AccountSummaryService.updateCustomerAccount(Seq(CustomerAccountSummaryResponse(1,1,"Prasad","Joshi","92212121", "SAVING", 5622, "prasad.joshi@in.ibm.com", "9432323923"),CustomerAccountSummaryResponse(2,1,"Prasad","Joshi","92212100", "CURRENT", 10090, "prasad.joshi@in.ibm.com", "9432323923")))
  AccountSummaryService.closeSession

}

object AccountSummaryService {

  println("Creating db configuration...")
  val db = Database.forConfig("db")

  val session = db.createSession()

  def closeSession = {
    session.close()
  }

  //creating TableQuery Object to perform database operations
  val customers: TableQuery[CustomerTable] = TableQuery[CustomerTable]

  val accounts: TableQuery[AccountTable] = TableQuery[AccountTable]

  val customerAccounts : TableQuery[CustomerAccountTable] = TableQuery[CustomerAccountTable]

  def insertCustomer(customer:Customer)={
    val insertAction: FixedSqlAction[Int, NoStream, Write] = customers.insertOrUpdate(customer)
    val f: Future[Int] = session.database.run(insertAction)
    f.mapTo[Int].map({ id => println("inserted : " + id)})
    Await.result(f, Duration.Inf)
  }

  def updateCustomer(customer: Customer) = {
    val q = customers.filter(_.id === customer.id)
    val updateAction: DBIO[Int] = q.update(customer)
    println("Query : "+ q.updateStatement)
    val f: Future[Int] = session.database.run(updateAction)
    f.mapTo[Int].map(count => println("Updated row : " + count))
    println("Waiting for Result...")
  }

  def deleteCustomer(id:Int) ={
    val q = customers.filter(_.id === id)
    val deleteAction: DBIO[Int] = q.delete
    val f: Future[Int] = session.database.run(deleteAction)
    println("Waiting for Result...")
    Await.result(f, Duration.Inf)
    f.onComplete(id =>{
      println("Completed delete operation")
      println("Deleted row id = " + id)
    }
    )
  }

  def getAllCustomers: Future[Seq[Customer]] = {
    println("Inside getAllCustomers method")
    val getAllCustomersQuery = for (c <- customers) yield c
    val getAllCustomersAction = getAllCustomersQuery.result
    println("Before Database execution")
    val getAllCustomersFuture: Future[Seq[Customer]] = session.database.run(getAllCustomersAction)
    println("Databse call executed...")
    getAllCustomersFuture
  }

  def getCustomerById(id:Int):Future[Customer]={
    val customerByIdQuery = customers.filter(customer => customer.id === id)
    val customerByIdAction = customerByIdQuery.result
    val customerByIdFuture: Future[Customer]  = session.database.run(customerByIdAction).map(_.head)
    customerByIdFuture
  }

  def insertAccount(account:Accounts)={
    val insertAction: DBIO[Int] =
      accounts returning accounts.map(_.id) += account
    val f: Future[Int] = session.database.run(insertAction)
    f.mapTo[Int].map({ id => println("inserted : " + id)})
    Await.result(f, Duration.Inf)
  }

  def updateAccounts(account: Accounts) = {
    val q = this.accounts.filter(_.id === account.id)
    val updateAction: DBIO[Int] = q.update(account)
    println("Query : "+ q.updateStatement)
    val f: Future[Int] = session.database.run(updateAction)
    f.mapTo[Int].map(count => println("Updated row : " + count))
    println("Waiting for Result...")
  }

  def deleteAccount(id:Int) ={
    val q = customers.filter(_.id === id)
    val deleteAction: DBIO[Int] = q.delete
    val f: Future[Int] = session.database.run(deleteAction)
    println("Waiting for Result...")
    Await.result(f, Duration.Inf)
    f.onComplete(id =>{
      println("Completed delete operation")
      println("Deleted row id = " + id)
    }
    )
  }

  def getAllAccounts: Future[Seq[Accounts]] = {
    println("Inside getAllCustomers method")
    val getAllAccountsQuery = for (c <- accounts) yield c
    val getAllAccountsAction = getAllAccountsQuery.result
    println("Before Database execution")
    val getAllAccountsFuture: Future[Seq[Accounts]] = session.database.run(getAllAccountsAction)
    println("Databse call executed...")
    getAllAccountsFuture
  }

  def getAccountById(id:Int):Future[Accounts]={
    val accountByIdQuery = accounts.filter(account => account.id === id)
    val accountByIdAction = accountByIdQuery.result
    val accountByIdFuture: Future[Accounts]  = session.database.run(accountByIdAction).map(_.head)
    accountByIdFuture
  }

  def doesCustomerAccountExist : Boolean = {
    val session = db.createSession()
    //val tables = Await.result(db.run(MTable.getTables), Duration.Inf).toList
    val tables = Await.result(session.database.run(MTable.getTables), Duration.Inf).toList
    println(tables.map(_.name.name))
    session.close()
    tables.map(_.name.name).contains("customeraccount")
  }

  def doesCustomerExist : Boolean = {
    val session = db.createSession()
    //val tables = Await.result(db.run(MTable.getTables), Duration.Inf).toList
    val tables = Await.result(session.database.run(MTable.getTables), Duration.Inf).toList
    println(tables.map(_.name.name))
    session.close()
    tables.map(_.name.name).contains("customer")
  }

  def doesAccountExist : Boolean = {
    val session = db.createSession()
    //val tables = Await.result(db.run(MTable.getTables), Duration.Inf).toList
    val tables = Await.result(session.database.run(MTable.getTables), Duration.Inf).toList
    println(tables.map(_.name.name))
    session.close()
    tables.map(_.name.name).contains("account")
  }

  def getCustomerAccountById(id:Int)={
    val customerAccountByIdQuery: Query[CustomerAccountTable,CustomerAccount,Seq] = customerAccounts.filter(customerAccount => customerAccount.aid === id)
    val customerQuery = customerAccountByIdQuery.flatMap(_.customer)

    val customerAccountByIdAction = customerQuery.result
    println(customerAccountByIdAction.statements)
    val customerAccountByIdFuture: Future[Seq[Customer]]  = session.database.run(customerAccountByIdAction)
    val customerAccountObj = Await.result(customerAccountByIdFuture,Duration.Inf)

    println(customerAccountObj)

  }

  def createTables = {
    //val session = db.createSession()
    if(!doesCustomerExist){
      println("Creating customer table ... :)")
      //Await.result(AccountSummaryService.db.run(AccountSummaryService.customers.schema.create),Duration.Inf)
      Await.result(session.database.run(AccountSummaryService.customers.schema.create),Duration.Inf)
      println("customer Table is created!!!")
    }else{
      println("We do not need to create customer table Again.. :)")
    }
    if(!doesAccountExist){
      println("Creating account table ... :)")
      //Await.result(AccountSummaryService.db.run(AccountSummaryService.accounts.schema.create),Duration.Inf)
      Await.result(session.database.run(AccountSummaryService.accounts.schema.create),Duration.Inf)
      println("account Table is created!!!")
    }else{
      println("We do not need to create account table Again.. :)")
    }
    if(!AccountSummaryService.doesCustomerAccountExist){
      println("Creating customeraccount table ... :)")
      //Await.result(AccountSummaryService.db.run(AccountSummaryService.customerAccounts.schema.create),Duration.Inf)
      Await.result(session.database.run(AccountSummaryService.customerAccounts.schema.create),Duration.Inf)
      println("customeraccount Table is created!!!")
    } else{
      println("We do not need to create table Again.. :)")
    }
    //session.close()
  }

  def insertCustomerAccount(customerAccount:CustomerAccount)={
    val insertAction: DBIO[Int] =
      customerAccounts returning customerAccounts.map(_.id) += customerAccount
    val f: Future[Int] = session.database.run(insertAction)
    f.mapTo[Int].map({ id => println("inserted : " + id)})
    Await.result(f, Duration.Inf)
  }

  def updateCustomerAccount(accounts:Seq[CustomerAccountSummaryResponse])={
    for(account <- accounts.iterator){
      val customerAccountByIdQuery = customerAccounts.filter(customerAccount => customerAccount.aid === account.aid)
      val customerAccountResult = Await.result(session.database.run(customerAccountByIdQuery.result),Duration.Inf)
      println(customerAccountResult)
      if(customerAccountResult.isEmpty){
        println("Customer Account not found!!!")
        val cid = AccountSummaryService.insertCustomer(Customer(0,account.fname,account.lname,account.mob,account.email))
        val aid = AccountSummaryService.insertAccount(Accounts(0,account.anumber,account.atype,null))
        AccountSummaryService.insertCustomerAccount(CustomerAccount(0,cid,aid,account.balance))
      }else{
        println("Customer Account found!!!" + customerAccountResult.size)
        for(customerAccount<-customerAccountResult.iterator){
          Await.result(db.run(customerAccountByIdQuery.map(_.balance).update(account.balance)),Duration.Inf)
        }
      }
    }

  }


}
