package com.capstone.surevenir.data.network.response

data class FavoriteResponse(
    val success: Boolean,
    val status_code: Int,
    val message: String,
    val data: FavoriteData
)

data class FavoriteData(
    val id: Int,
    val user_id: String,
    val product_id: Int,
    val createdAt: String,
    val updatedAt: String
)