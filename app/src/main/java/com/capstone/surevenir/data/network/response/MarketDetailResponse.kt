package com.capstone.surevenir.data.network.response

import com.capstone.surevenir.model.Image

data class MarketDetailResponse(
    val success: Boolean,
    val status_code: Int,
    val message: String,
    val data: MarketDetail
)

data class MarketDetail(
    val id: Int,
    val slug: String,
    val name: String,
    val description: String,
    val profile_image_url: String,
    val longitude: String,
    val latitude: String,
    val createdAt: String,
    val updatedAt: String,
    val images: List<ImageData>,
    
)
