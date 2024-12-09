package com.capstone.surevenir.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
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
    val images: List<String>,
    val merchant: Merchant
) : Parcelable

@Parcelize
data class ProductCheckout(
    val id: Int,
    val slug: String,
    val name: String,
    val description: String,
    val price: Int,
    val merchant_id: Int,
    val stock: Int,
    val updatedAt: String,
    val createdAt: String,
    val merchant: Merchant,
    val images: List<ImageCheckout>
) : Parcelable

@Parcelize
data class ImageCheckout(
    val id: Int,
    val item_id: Int,
    val url: String,
    val type: String
): Parcelable