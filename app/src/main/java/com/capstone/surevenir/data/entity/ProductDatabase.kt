package com.capstone.surevenir.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "products")
data class ProductDatabase(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "slug") val slug: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "price") val price: Int,
    @ColumnInfo(name = "merchantId") val merchantId: Int,
    @ColumnInfo(name = "stock") val stock: Int,
    @ColumnInfo(name = "createdAt") val createdAt: String,
    @ColumnInfo(name = "updatedAt") val updatedAt: String,
    @ColumnInfo(name = "categories") val categories: String,
    @ColumnInfo(name = "images") val images: String
)


