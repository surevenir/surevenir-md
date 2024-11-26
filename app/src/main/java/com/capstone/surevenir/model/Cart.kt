package com.capstone.surevenir.model

import java.util.Date

data class Cart(
    val id: Int,
    val userId: String,
    val productId: Int,
    val quantity: Int,
    val totalPrice: Float,
    val isCheckout: Boolean? = false,
    val createdAt: Date,
    val updatedAt: Date,
    val user: User,
    val product: Product
)