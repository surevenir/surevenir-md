package com.capstone.surevenir.data.network.response


data class ScanHistoryResponse(
    val from_cache: Boolean,
    val success: Boolean,
    val status_code: Int,
    val message: String,
    val timestamp: String,
    val data: List<ScanHistory>
)

data class ScanHistory(
    val id: Int,
    val slug: String,
    val predict: String,
    val accuration: Double,
    val image_url: String,
    val createdAt: String,
    val updatedAt: String,
    val user_id: String,
    val category_name: String,
    val category_description: String,
    val category_range_price: String
)
