package com.capstone.surevenir.model

data class Product(
    val id: Int,
    val slug: String,
    val name: String,
    val description: String,
    val price: Int,
    val merchant_id: Int,
    val stock: Int,
    val categories: List<Category>,
    val images: List<Image>
)