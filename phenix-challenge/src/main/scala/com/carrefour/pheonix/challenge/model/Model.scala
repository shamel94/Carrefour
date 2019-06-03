package com.carrefour.pheonix.challenge.model

case class ProductPrice(productId: String, price: String)

case class Transaction(id: String, date: String, shopId: String, productId: String, qty: Int, price: Double)

case class Sale(shopId: String, productId: String, quantity: Int)

case class Revenue(shopId: String, productId: String, revenue: Double)
