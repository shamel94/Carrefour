package com.carrefour.pheonix.challenge.utils

import java.util.logging.Logger

object ConversionUtil {
  val logger = Logger.getLogger(this.getClass.getName)

  def toInt(s: String): Option[Int] = {
    try {
      Some(s.toInt)
    } catch {
      case e: Exception => None
    }
  }


  def toDouble(s: String): Option[Double] = {
    try {
      Some(s.toDouble)
    } catch {
      case e: Exception => None
    }
  }


}
