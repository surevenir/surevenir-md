package com.capstone.surevenir.model

import java.util.Date

data class CheckoutDetails(
    val id: Int,
    val checkoutId: Int,
    val productId: Int,
    val totalPrice: Float,
    val createdAt: Date,
    val updatedAt: Date,
    val checkout: Checkout,
    val product: Product
)