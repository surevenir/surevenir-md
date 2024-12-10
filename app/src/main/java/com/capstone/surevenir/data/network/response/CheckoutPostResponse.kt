package com.capstone.surevenir.data.network.response

import com.google.gson.annotations.SerializedName

data class CheckoutPostResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("status_code")
    val statusCode: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: CheckoutPostData
)

data class CheckoutPostData(
    @SerializedName("cart")
    val cart: List<CartItem>,
    @SerializedName("checkout")
    val checkout: Checkout,
    @SerializedName("checkout_details")
    val checkoutDetails: List<CheckoutDetail>
)

data class Checkout(
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
    val updatedAt: String
)

data class CheckoutRequest(
    @SerializedName("cart_item_ids")
    val cartItemIds: List<Int>,
    @SerializedName("product_ids")
    val productIds: List<Int>
)