package com.capstone.surevenir.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Market(
    val id: Int,
    val slug: String,
    val name: String,
    val description: String?,
    @SerializedName("profile_image_url") // Nama key yang ada di JSON
    val profileImageUrl: String?,
    val longitude: String?,
    val latitude: String?,
    val createdAt: Date,
    val updatedAt: Date,
    var marketLocation: String? = null
)