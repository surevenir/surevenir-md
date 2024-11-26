package com.capstone.surevenir.model

import java.util.Date

data class Review(
    val id: Int,
    val rating: Int,
    val comment: String? = null,
    val userId: String,
    val productId: Int,
    val createdAt: Date,
    val updatedAt: Date,
    val user: User,
    val product: Product
)
