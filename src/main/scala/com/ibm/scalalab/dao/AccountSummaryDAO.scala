package com.ibm.scalalab.dao

import java.sql.Timestamp

import com.ibm.scalalab.domain.{CustomerAccount, Accounts, Customer}
import slick.lifted.{ForeignKeyQuery, Tag}
import slick.driver.MySQLDriver.api._
import slick.profile.SqlProfile.ColumnOption.Nullable


/**
 * Created by Nishant on 12/18/2015.
 */
object AccountSummaryDAO {

  class CustomerTable(tag:Tag)extends Table[Customer](tag,"customer"){
    def id = column[Int]("id",O.PrimaryKey,O.AutoInc)
    def fname = column[String]("fname")
    def lname = column[String]("lname")
    def mobnumber = column[String]("mobnumber")
    def email = column[String]("email")


    //  override def * = (id, fname, lname, mobnumber, email)
    def * = (id, fname,lname,mobnumber,email) <> (Customer.tupled, Customer.unapply _)
  }

  class AccountTable(tag:Tag)extends Table[Accounts](tag,"account"){
    def id = column[Int]("id",O.PrimaryKey,O.AutoInc)
    def aid = column[String]("aid")
    def atype = column[String]("atype")
    def creationdate = column[String]("creationdate",Nullable)

    //  override def * = (id, fname, lname, mobnumber, email)
    def * = (id, aid,atype,creationdate) <> (Accounts.tupled, Accounts.unapply _)

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
