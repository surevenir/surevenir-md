package com.capstone.surevenir.model

data class Merchant(
    val id: Int,
    val name: String,
    val profile_image_url: String?,
    val description: String?,
    val longitude: String?,
    val latitude: String?,
    val user_id: String?,
    val market_id: Int?,
    val createdAt: String?,
    val updatedAt: String?,
    val shopLocation: String?,
    val products_count: Int,
)
