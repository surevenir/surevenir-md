package com.capstone.surevenir.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "merchants")
data class MerchantDatabase(
    @PrimaryKey
    val id: Int,
    val name: String,
    @ColumnInfo(name = "profile_image_url")
    val profileImageUrl: String?,
    val description: String?,
    val longitude: String?,
    val latitude: String?,
    @ColumnInfo(name = "user_id")
    val userId: String?,
    @ColumnInfo(name = "market_id")
    val marketId: Int?,
    @ColumnInfo(name = "created_at")
    val createdAt: String?,
    @ColumnInfo(name = "updated_at")
    val updatedAt: String?,
    @ColumnInfo(name = "products_count")
    val productsCount: Int = 0,
    val location: String?
)