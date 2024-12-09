package com.capstone.surevenir.data.network.response

data class MerchantResponse(
    val success: Boolean,
    val status_code: Int,
    val message: String,
    val data: List<MerchantData>
)

data class MerchantData(
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
    val product_count: Int,
    val location: String,
    val images: List<Any>? = null // atau tipe data berbeda, jika ingin mengabaikan isinya
)
