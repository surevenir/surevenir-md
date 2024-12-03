package com.capstone.surevenir.model

import java.util.Date

data class Category(
    val id: Int,
    val name: String,
    val description: String?,

    val image_url: String?,
    val range_price: String?,
    val createdAt: String,
    val updatedAt: String,
)
