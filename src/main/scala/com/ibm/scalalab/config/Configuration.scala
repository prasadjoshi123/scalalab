package com.ibm.scalalab.config

import com.typesafe.config.ConfigFactory
import scala.util.Try

/**
 * @usecase : provides configuration details of REST Service like port number, host.
 * @author : Mandar Dadpe
 */
trait Configuration {
  /**
   * Application config object.
   */
  val config = ConfigFactory.load()

  /** Host name/address to start service on. Lazy vals are called for the first time and then value is stored */
  lazy val serviceHost = Try(config.getString("service.host")).getOrElse("localhost")

  /** Port to start service on. */
  lazy val servicePort = Try(config.getInt("service.port")).getOrElse(8081)
}
