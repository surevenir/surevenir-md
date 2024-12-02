package com.capstone.surevenir.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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

@Parcelize
data class ShopData(
    val location: String,
    val productsCount: Int
): Parcelable