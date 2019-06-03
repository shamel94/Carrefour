package com.carrefour.pheonix.challenge.utils

import java.util.Calendar
import java.util.logging.Logger


object Utils {
  val logger = Logger.getLogger(this.getClass.getName)

  def getLast7Days(date: String): List[String] = {
    val format = new java.text.SimpleDateFormat("yyyyMMdd")
    val currentDay = format.parse(date)
    val cal = Calendar.getInstance()
    val days = (1 to 6).map(
      offset => {
        cal.setTime(currentDay)
        cal.add(Calendar.DATE, -offset)
        format.format(cal.getTime)
      })
    days.toList
  }
}