package com.capstone.surevenir.model

import java.util.Date

//Tes Lagi

data class User(
    val id: String,
    val fullName: String,
    val username: String,
    val email: String,
    val password: String? = null,
    val phone: String? = null,
    val role: String = "USER",
    val provider: String = "EMAIL",
    val longitude: String? = null,
    val latitude: String? = null,
    val address: String? = null,
    val profileImageUrl: String? = null,
    val createdAt: Date,
    val updatedAt: Date,
    val merchant: Merchant? = null,
    val reviews: List<Review> = emptyList(),
    val carts: List<Cart> = emptyList(),
    val checkouts: List<Checkout> = emptyList()
)
