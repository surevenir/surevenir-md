package com.capstone.surevenir.data.network.response

import android.os.Parcelable
import com.capstone.surevenir.model.Product
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class CartResponse(
    @SerializedName("status_code")
    val statusCode: Int? = null,
    @SerializedName("success")
    val success: Boolean? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("data")
    val data: CartData
)

data class CartData(
    @SerializedName("cart")
    val cart: List<CartItem>,
    @SerializedName("total_price")
    val totalPrice: Int
)

@Parcelize
data class CartItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("product_id")
    val productId: Int,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("product")
    val product: Product,
    @SerializedName("subtotal_price")
    val subtotalPrice: Int
) : Parcelable

data class DeleteCartResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("status_code")
    val statusCode: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: CartData
)
