package com.carrefour.pheonix.challenge.utils

import java.io.File
import java.nio.file.{Files, Paths}
import java.util.logging.Logger

import com.carrefour.pheonix.challenge.config.Constants._
import com.carrefour.pheonix.challenge.model.{Revenue, Sale, Transaction}

object FileUtils {
  val logger = Logger.getLogger(this.getClass.getName)
  def getLast7DaysProcessedTransactions(srcDir: String, days: List[String]): List[File] = {

    var result = List.empty[File]
    days.map(day => {
      val d = new File(srcDir + TRANSACTION_PREFIX_FILENAME + day + DATA_FILE_EXTENSION + PROCESSED_DATA_FILE_EXTENSION)
      if (d.isFile)
        result = d :: result
    })
    result

  }


  def writeTransactionsInFile(folder: String, file: File, transactions: List[Transaction]) = {
    val filename = file.getName + PROCESSED_DATA_FILE_EXTENSION
    val sb = new StringBuffer()

    for (elem <- transactions) {
      sb.append(elem.id + COLUMN_SEPARATOR
        + elem.date + COLUMN_SEPARATOR
        + elem.shopId + COLUMN_SEPARATOR
        + elem.productId + COLUMN_SEPARATOR
        + elem.qty + COLUMN_SEPARATOR +
        elem.price + "\n")
    }
    Files.write(Paths.get(folder  + filename), sb.toString.getBytes())
  }


  def writeMapOfListOfRevenues(topRevenuesByShop: Map[String, List[Revenue]], date: String, destDir: String) = {

    topRevenuesByShop.foreach(g => {
      val filename = destDir +  TOP_REVENUES_PREFIX_FILENAME + g._1 + "_" + date + DATA_FILE_EXTENSION
      writeListOfRevenues(filename, g._2)

    })
  }


  def writeListOfSales(filename: String, sales: List[Sale]): Unit = {
    val salesByShopContent = new StringBuffer()
    sales.foreach(a => {
      salesByShopContent.append(a.productId + COLUMN_SEPARATOR + a.quantity + CR_LF)
    })
    Files.write(Paths.get(filename), salesByShopContent.toString.getBytes())
  }

  def writeListOfRevenues(filename: String, sales: List[Revenue]): Unit = {
    val salesByShopContent = new StringBuffer()
    sales.foreach(a => {
      salesByShopContent.append(a.productId + COLUMN_SEPARATOR + a.revenue + CR_LF)
    })
    Files.write(Paths.get(filename), salesByShopContent.toString.getBytes())
  }

  def writeMapOfListOfSales(mapOfListOfSales: Map[String, List[Sale]], date: String, destDir: String, isWeekly: Boolean): Unit = {

    mapOfListOfSales.foreach(g => {
      var filename = ""
      if (! isWeekly)
        filename = destDir + TOP_SALES_PREFIX_FILENAME + g._1 + "_" + date + DATA_FILE_EXTENSION
      else
        filename = destDir + TOP_SALES_PREFIX_FILENAME + g._1 + "_" + date + "-J7" + DATA_FILE_EXTENSION

      writeListOfSales(filename, g._2)

    })
  }


  def sortTransactionFileByDate(f1: File, f2: File): Boolean = {
    val date1 = f1.getName.replace(TRANSACTION_PREFIX_FILENAME, "").replace(".data", "")
    val date2 = f2.getName.replace(TRANSACTION_PREFIX_FILENAME, "").replace(".data", "")
    date1.compareTo(date2) < 0
  }

  /**
    * Get list of transactions files
    *
    * @param dir
    * @return
    */
  def getListOfTransactionFiles(dir: String): List[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      d.listFiles
        .filter(_.isFile)
        .filter(_.getName.startsWith(TRANSACTION_PREFIX_FILENAME))
        .filter(!_.getName.endsWith(PROCESSED_DATA_FILE_EXTENSION))

        .toList.sortWith(sortTransactionFileByDate)
    } else {
      List.empty[File]
    }
  }

  /**
    * Get list of referentiel products File of current date
    *
    * @param date
    * @param dir
    * @return
    */
  def getRefProductByDate(date: String, dir: String): List[File] = {

    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      d.listFiles
        .filter(n => n.isFile)
        .filter(n => n.getName.startsWith(REFERENCE_PROD) && n.getName.endsWith(date + DATA_FILE_EXTENSION))
        .toList
    } else {
      List[File]()
    }
  }


}
