package com.carrefour.pheonix.challenge.config

object Constants {
  val COLUMN_SEPARATOR: Char = '|'
  val CR_LF = "\r\n"
  val TRANSACTION_PREFIX_FILENAME = "transactions_"
  val DATA_FILE_EXTENSION = ".data"
  val PROCESSED_DATA_FILE_EXTENSION = ".processed"
  val REFERENCE_PROD = "reference_prod-"
  val TOP = 100
  val TOP_SALES_PREFIX_FILENAME = "top_100_ventes_"
  val TOP_SALES_GLOBAL_PREFIX_FILENAME = "top_100_ventes_GLOBAL_"
  val TOP_REVENUES_GLOBAL_PREFIX_FILENAME = "top_100_ca_GLOBAL_"

  val TOP_REVENUES_PREFIX_FILENAME = "top_100_ca_"


  /** Transaction columns */
  val TRX_NUMBER_OF_COLUMNS = 5

  val TRX_TRX_ID_COLUMN = 0
  val TRX_DATE_COLUMN = 1
  val TRX_SHOP_ID_COLUMN = 2
  val TRX_PRODUCT_ID_COLUMN = 3
  val TRX_QUANTITY_COLUMN = 4
  val TRX_PRICE_COLUMN = 5


}
