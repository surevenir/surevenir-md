package com.capstone.surevenir.data.network.response

import com.capstone.surevenir.model.Category
import com.capstone.surevenir.model.Image
import com.capstone.surevenir.model.Market
import com.capstone.surevenir.model.Product

data class ProductResponse(
    val statusCode: Int? = null,
    val data: List<ProductData>? = null,
    val success: Boolean? = null,
    val message: String? = null
)

//dsada

data class ProductData(
    val id: Int,
    val slug: String,
    val name: String,
    val description: String,
    val price: Int,
    val merchant_id: Int,
    val stock: Int,
    val createdAt: String,
    val updatedAt: String,
    val categories: List<Category>,
    val images: List<Image>
)
