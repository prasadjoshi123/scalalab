package com.ibm.scalalab.dao

import com.ibm.scalalab.domain.{Accounts, Customer, CustomerAccount}
import slick.driver.MySQLDriver.api._
import slick.lifted.{ForeignKeyQuery, Tag}
import slick.profile.SqlProfile.ColumnOption.{NotNull, Nullable}


/**
 * Created by Nishant on 12/18/2015.
 */
object AccountSummaryDAO {

  class CustomerTable(tag:Tag)extends Table[Customer](tag,"customer"){
    def id = column[Int]("id",O.PrimaryKey,O.AutoInc)
    def fname = column[String]("fname")
    def lname = column[String]("lname")
    def mobnumber = column[String]("mobnumber",O.Length(15),NotNull)
    def email = column[String]("email")
    def index_mobnumber = index("index_mobnumber",mobnumber,unique = true)
    //  override def * = (id, fname, lname, mobnumber, email)
    def * = (id, fname,lname,mobnumber,email) <> (Customer.tupled, Customer.unapply _)
  }

  class AccountTable(tag:Tag)extends Table[Accounts](tag,"account"){
    def id = column[Int]("id",O.PrimaryKey,O.AutoInc)
    def anumber = column[String]("anumber" ,O.Length(10), NotNull )
    def atype = column[String]("atype")
    def creationdate = column[String]("creationdate",Nullable)
    def index_anumber = index("idx_anumber",anumber,unique = true)
    //  override def * = (id, fname, lname, mobnumber, email)
    def * = (id,anumber,atype,creationdate) <> (Accounts.tupled, Accounts.unapply _)

  }

  class CustomerAccountTable(tag:Tag)extends Table[CustomerAccount](tag,"customeraccount"){
    def id = column[Int]("id",O.PrimaryKey,O.AutoInc)
    def cid = column[Int]("cid")
    def aid = column[Int]("aid")
    def balance = column[Double]("balance")

    //  override def * = (id, fname, lname, mobnumber, email)
    def * = (id, cid,aid,balance) <> (CustomerAccount.tupled, CustomerAccount.unapply _)
    def customer: ForeignKeyQuery[CustomerTable, Customer] =
      foreignKey("customer", cid, TableQuery[CustomerTable])(_.id)
    def account: ForeignKeyQuery[AccountTable, Accounts] =
      foreignKey("account", aid, TableQuery[AccountTable])(_.id)
  }
}
