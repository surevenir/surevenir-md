package com.capstone.surevenir.data.network.response

import com.capstone.surevenir.model.Category
import com.capstone.surevenir.model.Image
import com.capstone.surevenir.model.Merchant

data class ProductDetailResponse(
    val success: Boolean,
    val status_code: Int,
    val message: String,
    val data: ProductDetail
)

data class ProductDetail(
    val id: Int,
    val slug: String,
    val name: String,
    val description: String,
    val price: Int,
    val merchant_id: Int,
    val stock: Int,
    val createdAt: String,
    val updatedAt: String,
    val merchant: Merchant,
    val product_categories: List<Category>,
    val images: List<Image>
)