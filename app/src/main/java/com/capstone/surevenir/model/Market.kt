package com.capstone.surevenir.model

import java.util.Date

data class Market(
    val id: Int,
    val slug: String,
    val name: String,
    val description: String?,
    val profileImageUrl: String?,
    val longitude: String?,
    val latitude: String?,
    val createdAt: Date,
    val updatedAt: Date,
    var marketLocation: String? = null
)