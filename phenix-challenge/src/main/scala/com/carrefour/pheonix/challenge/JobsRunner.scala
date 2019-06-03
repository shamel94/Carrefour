package com.carrefour.pheonix.challenge

import java.io.File
import java.io.File
import java.nio.file.{Files, Paths}
import java.util.logging.Logger

import com.carrefour.pheonix.challenge.config.Constants._
import com.carrefour.pheonix.challenge.model.{Revenue, Transaction}
import com.carrefour.pheonix.challenge.utils.{FileUtils, TransactionProcessor, Utils}

import scala.io.Source

object JobsRunner extends App {
  val logger = Logger.getLogger(this.getClass.getName)
  //private val INPUT_FOLDER = "C:\\dev\\phenix\\pro\\scala-first-project\\src\\main\\data\\"
  //private val OUTPUT_FOLDER = "C:\\dev\\phenix\\pro\\scala-first-project\\src\\main\\data\\"

  logger.info("Start processing..." )
  if (args.length != 2) {
    logger.warning("Missing file path parameters : " +
      "\n\t - param 1 : Input Folder" +
      "\n\t - param 2 : Output Folder"
    )
    sys.exit(1)
  }
  val INPUT_FOLDER = args(0)
  val OUTPUT_FOLDER = args(1)

  val transactionFiles = FileUtils.getListOfTransactionFiles(INPUT_FOLDER)
  processTransactions(transactionFiles)


  /**
    *
    * @param tFiles
    */
  def processTransactions(tFiles: List[File]): Unit = {
    tFiles.foreach(processTransaction)
  }



  /**
    * Transaction processing
    * @param file
    */
  def processTransaction(file: File) = {


    val isProcessed = new File(INPUT_FOLDER + file.getName + PROCESSED_DATA_FILE_EXTENSION).isFile
    if (!isProcessed) {

      logger.info("process transaction " + file)
      val date = file.getName.replace(TRANSACTION_PREFIX_FILENAME, "").replace(".data", "")
      val productPricesList = FileUtils.getRefProductByDate(date, INPUT_FOLDER)
      val mapOfPricesByShop = TransactionProcessor.buildMapOfPricesByShop(date, productPricesList, INPUT_FOLDER)
      val transactions = TransactionProcessor.enrichTransaction(file, mapOfPricesByShop, INPUT_FOLDER)


      val topSalesByShop = TransactionProcessor.getTopSalesByShop(transactions)
      FileUtils.writeMapOfListOfSales(topSalesByShop, date, OUTPUT_FOLDER, false)


      val topSales = TransactionProcessor.getTopSales(transactions)
      var filename = OUTPUT_FOLDER + TOP_SALES_GLOBAL_PREFIX_FILENAME + date + DATA_FILE_EXTENSION
      FileUtils.writeListOfSales(filename, topSales)


      val topRevenuesByShop = TransactionProcessor.getTopRevenuesByShop(transactions)
      FileUtils.writeMapOfListOfRevenues(topRevenuesByShop, date, OUTPUT_FOLDER)


      val topRevenues = TransactionProcessor.getTopRevenues(transactions)
      filename = OUTPUT_FOLDER + TOP_REVENUES_GLOBAL_PREFIX_FILENAME + date + DATA_FILE_EXTENSION
      FileUtils.writeListOfRevenues(filename, topRevenues)

      // ecrire les transactions dans un fichier pour re-utilsiation à j-7
      FileUtils.writeTransactionsInFile(INPUT_FOLDER, file, transactions)

      // recuperer les transactions déjà processées vielles de 7 jours
      val last7Days = Utils.getLast7Days(date)

      val lastTransactionsFile = FileUtils.getLast7DaysProcessedTransactions(INPUT_FOLDER, last7Days)

      val topSales7 = TransactionProcessor.getTopSales7(transactions, lastTransactionsFile, INPUT_FOLDER)

      FileUtils.writeMapOfListOfSales(topSales7, date, OUTPUT_FOLDER, true)
    }else{

    }
  }
}
