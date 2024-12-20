package com.capstone.surevenir.data.network.response

data class ProductResponse(
    val statusCode: Int? = null,
    val data: List<ProductData>? = null,
    val success: Boolean? = null,
    val message: String? = null
)

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
    val product_categories: List<ProductCategory>,
    val images: List<ImageData>
)

data class ProductCategory(
    val category: Category
)

data class Category(
    val id: Int,
    val name: String
)

data class ImageData(
    val url: String
)
