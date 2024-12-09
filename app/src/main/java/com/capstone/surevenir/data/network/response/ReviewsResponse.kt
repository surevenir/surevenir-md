package com.capstone.surevenir.data.network.response

import com.google.gson.annotations.SerializedName

data class ReviewResponse(
	@SerializedName("success")
	val success: Boolean,
	@SerializedName("status_code")
	val statusCode: Int,
	@SerializedName("message")
	val message: String,
	@SerializedName("data")
	val data: ReviewData
)

data class ReviewData(
	@SerializedName("id")
	val id: Int,
	@SerializedName("rating")
	val rating: Int,
	@SerializedName("comment")
	val comment: String,
	@SerializedName("user_id")
	val userId: String,
	@SerializedName("product_id")
	val productId: Int,
	@SerializedName("createdAt")
	val createdAt: String,
	@SerializedName("updatedAt")
	val updatedAt: String,
	@SerializedName("images")
	val images: List<ReviewImage>
)

data class ReviewImage(
	@SerializedName("url")
	val url: String,
	@SerializedName("itemId")
	val itemId: Int,
	@SerializedName("type")
	val type: String
)

data class ReviewRequest(
	@SerializedName("rating")
	val rating: Int,
	@SerializedName("comment")
	val comment: String,
	@SerializedName("user_id")
	val userId: String,
	@SerializedName("product_id")
	val productId: Int,
	@SerializedName("images")
	val images: List<ReviewImageRequest>
)

data class ReviewImageRequest(
	@SerializedName("url")
	val url: String,
	@SerializedName("type")
	val type: String
)