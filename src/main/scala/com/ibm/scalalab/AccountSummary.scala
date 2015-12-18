package com.ibm.scalalab

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import com.ibm.scalalab.actor.{AccountHttpServiceActor, ServiceRouting}
import com.ibm.scalalab.config.Configuration
import spray.can.Http

/**
 * launcher to run spray server which extends standard scala App trait
 */
object AccountSummary extends App with Configuration {
  //Create an Actor System
  implicit var system = ActorSystem("User-Banking-Service-Actor")

  private implicit val _ = system.dispatcher
  //Create routes for Rest Service
  val accountServiceRoutes = new ServiceRouting().route
  //Create an ActorRef to the HttpService and Actor
  val accountService = system.actorOf(Props(new AccountHttpServiceActor(accountServiceRoutes)))
  //Create a Http Server
  IO(Http)(system) ! Http.Bind(accountService, serviceHost, servicePort)
}
