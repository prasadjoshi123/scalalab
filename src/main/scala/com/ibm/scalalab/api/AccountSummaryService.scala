package com.ibm.scalalab.api

import java.util.Calendar
import java.sql.{Timestamp, Date}
import com.ibm.scalalab.api
import com.ibm.scalalab.dao.AccountSummaryDAO.{CustomerAccountTable, AccountTable, CustomerTable}
import com.ibm.scalalab.domain.{CustomerAccountSummaryResponse, CustomerAccount, Accounts, Customer}
import slick.lifted.TableQuery
import slick.dbio.DBIO
import slick.jdbc.meta.MTable
import slick.jdbc.JdbcBackend.Database
import slick.lifted.{ProvenShape, ForeignKeyQuery}
import slick.driver.MySQLDriver.api._
import slick.profile.SqlProfile.ColumnOption.{Nullable, NotNull}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future,ExecutionContext}

/**
 * Created by Nishant on 12/18/2015.
 */
object AccountSummaryServiceTest extends App{
  AccountSummaryService.createTables

}

object AccountSummaryService {

  println("Creating db configuration...")
  val db = Database.forConfig("db")

  //creating TableQuery Object to perform database operations
  val customers: TableQuery[CustomerTable] = TableQuery[CustomerTable]

  val accounts: TableQuery[AccountTable] = TableQuery[AccountTable]

  val customerAccounts : TableQuery[CustomerAccountTable] = TableQuery[CustomerAccountTable]

  def insertCustomer(customer:Customer)={
    val insertAction: DBIO[Int] =
      customers returning customers.map(_.id) += customer
    val f: Future[Int] = db.run(insertAction)
    f.mapTo[Int].map({ id => println("inserted : " + id)})
    Await.result(f, Duration.Inf)
  }

  def updateCustomer(customer: Customer) = {
    val q = customers.filter(_.id === customer.id)
    val updateAction: DBIO[Int] = q.update(customer)
    println("Query : "+ q.updateStatement)
    val f: Future[Int] = db.run(updateAction)
    f.mapTo[Int].map(count => println("Updated row : " + count))
    println("Waiting for Result...")
  }

  def deleteCustomer(id:Int) ={
    val q = customers.filter(_.id === id)
    val deleteAction: DBIO[Int] = q.delete
    val f: Future[Int] = db.run(deleteAction)
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
    val getAllCustomersFuture: Future[Seq[Customer]] = db.run(getAllCustomersAction)
    println("Databse call executed...")
    getAllCustomersFuture
  }

  def getCustomerById(id:Int):Future[Customer]={
    val customerByIdQuery = customers.filter(customer => customer.id === id)
    val customerByIdAction = customerByIdQuery.result
    val customerByIdFuture: Future[Customer]  = db.run(customerByIdAction).map(_.head)
    customerByIdFuture
  }

  def insertAccount(account:Accounts)={
    val insertAction: DBIO[Int] =
      accounts returning accounts.map(_.id) += account
    val f: Future[Int] = db.run(insertAction)
    f.mapTo[Int].map({ id => println("inserted : " + id)})
    Await.result(f, Duration.Inf)
  }

  def updateAccounts(account: Accounts) = {
    val q = this.accounts.filter(_.id === account.id)
    val updateAction: DBIO[Int] = q.update(account)
    println("Query : "+ q.updateStatement)
    val f: Future[Int] = db.run(updateAction)
    f.mapTo[Int].map(count => println("Updated row : " + count))
    println("Waiting for Result...")
  }

  def deleteAccount(id:Int) ={
    val q = customers.filter(_.id === id)
    val deleteAction: DBIO[Int] = q.delete
    val f: Future[Int] = db.run(deleteAction)
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
    val getAllAccountsFuture: Future[Seq[Accounts]] = db.run(getAllAccountsAction)
    println("Databse call executed...")
    getAllAccountsFuture
  }

  def getAccountById(id:Int):Future[Accounts]={
    val accountByIdQuery = accounts.filter(account => account.id === id)
    val accountByIdAction = accountByIdQuery.result
    val accountByIdFuture: Future[Accounts]  = db.run(accountByIdAction).map(_.head)
    accountByIdFuture
  }

  def doesCustomerAccountExist : Boolean = {
    val tables = Await.result(db.run(MTable.getTables), Duration.Inf).toList
    println(tables.map(_.name.name))
    tables.map(_.name.name).contains("customeraccount")
  }

  def doesCustomerExist : Boolean = {
    val tables = Await.result(db.run(MTable.getTables), Duration.Inf).toList
    println(tables.map(_.name.name))
    tables.map(_.name.name).contains("customer")
  }

  def doesAccountExist : Boolean = {
    val tables = Await.result(db.run(MTable.getTables), Duration.Inf).toList
    println(tables.map(_.name.name))
    tables.map(_.name.name).contains("account")
  }

  def getCustomerAccountById(id:Int)={
    val customerAccountByIdQuery: Query[CustomerAccountTable,CustomerAccount,Seq] = customerAccounts.filter(customerAccount => customerAccount.aid === id)
    val customerQuery = customerAccountByIdQuery.flatMap(_.customer)

    val customerAccountByIdAction = customerQuery.result
    println(customerAccountByIdAction.statements)
    val customerAccountByIdFuture: Future[Seq[Customer]]  = db.run(customerAccountByIdAction)
    val customerAccountObj = Await.result(customerAccountByIdFuture,Duration.Inf)

    println(customerAccountObj)

  }

  def createTables = {
    if(!doesCustomerExist){
      println("Creating customer table ... :)")
      Await.result(AccountSummaryService.db.run(AccountSummaryService.customers.schema.create),Duration.Inf)
      println("customer Table is created!!!")
    }else{
      println("We do not need to create customer table Again.. :)")
    }
    if(!doesAccountExist){
      println("Creating account table ... :)")
      Await.result(AccountSummaryService.db.run(AccountSummaryService.accounts.schema.create),Duration.Inf)
      println("account Table is created!!!")
    }else{
      println("We do not need to create account table Again.. :)")
    }
    if(!AccountSummaryService.doesCustomerAccountExist){
      println("Creating customeraccount table ... :)")
      Await.result(AccountSummaryService.db.run(AccountSummaryService.customerAccounts.schema.create),Duration.Inf)
      println("customeraccount Table is created!!!")
    } else{
      println("We do not need to create table Again.. :)")
    }
  }

  def insertCustomerAccount(customerAccount:CustomerAccount)={
    val insertAction: DBIO[Int] =
      customerAccounts returning customerAccounts.map(_.id) += customerAccount
    val f: Future[Int] = db.run(insertAction)
    f.mapTo[Int].map({ id => println("inserted : " + id)})
    Await.result(f, Duration.Inf)
  }

  def updateCustomerAccount(accounts:Seq[CustomerAccountSummaryResponse])={
    for(account <- accounts.iterator){
      val customerAccountByIdQuery = customerAccounts.filter(customerAccount => customerAccount.aid === account.aid)
      val customerAccountResult = Await.result(db.run(customerAccountByIdQuery.result),Duration.Inf)
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
