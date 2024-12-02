package com.capstone.surevenir.model

data class Product(
    val id: Int,
    val slug: String,
    val name: String,
    val description: String,
    val price: Int,
    val merchant_id: Int,
    val stock: Int,
    val updatedAt: String,
    val createdAt: String,
    val categories: List<String>,
    val images: List<String>
)