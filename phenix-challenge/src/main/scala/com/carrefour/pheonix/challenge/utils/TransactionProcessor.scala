package com.carrefour.pheonix.challenge.utils

import java.io.File
import java.util.logging.Logger

import com.carrefour.pheonix.challenge.config.Constants._
import com.carrefour.pheonix.challenge.model.{Revenue, Sale, Transaction}

import scala.concurrent.Future
import scala.io.Source

object TransactionProcessor {
  val logger = Logger.getLogger(this.getClass.getName)

  def mergeTransactions(transactions: List[Transaction], lastTransactionsFile: List[File], srcDir: String) = {
    var allTransactions = List.empty[Transaction]
    allTransactions = allTransactions ::: transactions
    lastTransactionsFile.foreach(
      t => {
        for (line <- Source.fromFile(srcDir + t.getName).getLines) {
          val split = line.split(COLUMN_SEPARATOR)

          val qty = ConversionUtil.toInt(split(TRX_QUANTITY_COLUMN)).getOrElse(0)
          val price = ConversionUtil.toDouble(split(TRX_PRICE_COLUMN)).getOrElse(0.0)

          allTransactions = Transaction(
            split(TRX_TRX_ID_COLUMN),
            split(TRX_DATE_COLUMN),
            split(TRX_SHOP_ID_COLUMN),
            split(TRX_PRODUCT_ID_COLUMN),
            qty,
            price) :: allTransactions


        }

      })
    allTransactions
  }

  def getTopSales7(transactions: List[Transaction], lastTransactionsFile: List[File], srcDir: String) = {


    val allTransactions = mergeTransactions(transactions, lastTransactionsFile, srcDir)
    getTopSalesByShop(allTransactions)


  }


  def getTopRevenues(transactions: List[Transaction]): List[Revenue] = {
    transactions
      .groupBy(t => t.productId)
      .map(g => g._2.reduce((a, b) => a.copy(price = a.qty * a.price + b.qty * b.price)))
      .toList.sortWith((t1, t2) => t1.price > t2.price)
      .take(TOP)
      .map(t => Revenue(t.shopId, t.productId, t.price))
  }


  def getTopRevenuesByShop(transactions: List[Transaction]): Map[String, List[Revenue]] = {

    transactions
      .groupBy(t => (t.shopId, t.productId))
      .map(g => g._2.reduce((a, b) => a.copy(price = a.qty * a.price + b.qty * b.price)))
      .map(t => Revenue(t.shopId, t.productId, t.price))
      .groupBy(t => t.shopId)
      .map(g => {
        (g._1, g._2.toList.sortWith((t1, t2) => t1.revenue > t2.revenue).take(TOP))
      })
  }


  /**
    * Build map of price by store
    *
    * @param date
    * @param productPricesList
    * @return
    */
  def buildMapOfPricesByShop(date: String, productPricesList: List[File], srcDir: String): Map[String, Map[String, Double]] = {

    var mapOfPricesByShop = Map.empty[String, Map[String, Double]]

    productPricesList.foreach(
      priceFile => {
        val shopId = priceFile.getName.replace(REFERENCE_PROD, "").replace("_" + date + DATA_FILE_EXTENSION, "")
        var mapOfPrices = Map.empty[String, Double]
        for (line <- Source.fromFile(srcDir + priceFile.getName).getLines) {
          val split = line.split(COLUMN_SEPARATOR)
          split.length match {
            case 2 => {
              val price = ConversionUtil.toDouble(split(1)).getOrElse(0.0)
              val productId = split(0)
              mapOfPrices = mapOfPrices + (productId -> price)
            }
          }
        }
        mapOfPricesByShop = mapOfPricesByShop + (shopId -> mapOfPrices)
      }
    )
    mapOfPricesByShop
  }

  /**
    * Enriching transactions with prices
    *
    * @param file
    * @param mapOfPricesByShop
    * @return
    */
  def enrichTransaction(file: File, mapOfPricesByShop: Map[String, Map[String, Double]], srcDir: String): List[Transaction] = {
    var result = List.empty[Transaction]
    for (line <- Source.fromFile(srcDir + file.getName).getLines) {

      val split = line.split(COLUMN_SEPARATOR)
      split.length match {
        case TRX_NUMBER_OF_COLUMNS => {
          val shopId = line.split('|')(TRX_SHOP_ID_COLUMN)
          val productId = line.split('|')(TRX_PRODUCT_ID_COLUMN)
          val price = mapOfPricesByShop.getOrElse(shopId, Map.empty).getOrElse(productId, 0.0)
          val qty = ConversionUtil.toInt(split(TRX_QUANTITY_COLUMN)).getOrElse(0)
          result = result :+ Transaction(split(TRX_TRX_ID_COLUMN), split(TRX_DATE_COLUMN), shopId, productId, qty, price)
        }
      }
    }
    result
  }

  def getTopSalesByShop(transactions: List[Transaction]): Map[String, List[Sale]] = {

    val groups =
      transactions
        .groupBy(t => (t.shopId, t.productId))
        .map(g => g._2.reduce((a, b) => a.copy(qty = a.qty + b.qty)))
        .map(t => Sale(t.shopId, t.productId, t.qty))
        .groupBy(t => t.shopId)
        .map(g => {
          (g._1, g._2.toList.sortWith((t1, t2) => t1.quantity > t2.quantity).take(TOP))
        })

    groups


  }

  def getTopSales(transactions: List[Transaction]): List[Sale] = {
    val sales = transactions
      .groupBy(t => t.productId)
      .map(g => g._2.reduce((a, b) => a.copy(qty = a.qty + b.qty)))
      .toList.sortWith((t1, t2) => t1.qty > t2.qty)
      .take(TOP)
      .map(t => Sale(t.shopId, t.productId, t.qty))
    sales
  }


}
