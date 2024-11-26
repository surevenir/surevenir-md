package com.capstone.surevenir.model

import java.util.Date

data class Checkout(
    val id: Int,
    val userId: String,
    val totalPrice: Float,
    val createdAt: Date,
    val updatedAt: Date,
    val user: User,
    val checkoutDetails: List<CheckoutDetails>
)
