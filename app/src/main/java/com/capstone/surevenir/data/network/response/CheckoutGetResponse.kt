package com.capstone.surevenir.data.network.response

import android.os.Parcelable
import com.capstone.surevenir.model.ProductCheckout
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class CheckoutGetResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("status_code")
    val statusCode: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: List<CheckoutData>
)

@Parcelize
data class CheckoutData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("total_price")
    val totalPrice: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("checkout_details")
    val checkoutDetails: List<CheckoutDetail>
) : Parcelable

@Parcelize
data class CheckoutDetail(
    @SerializedName("id")
    val id: Int,
    @SerializedName("checkout_id")
    val checkoutId: Int,
    @SerializedName("product_id")
    val productId: Int,
    @SerializedName("product_identity")
    val productIdentity: String,
    @SerializedName("product_quantity")
    val productQuantity: Int,
    @SerializedName("product_price")
    val productPrice: Double,
    @SerializedName("product_subtotal")
    val productSubtotal: Double,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("product")
    val product: ProductCheckout
) : Parcelable
