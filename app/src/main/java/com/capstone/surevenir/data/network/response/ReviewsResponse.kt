package com.capstone.surevenir.data.network.response

import com.google.gson.annotations.SerializedName

data class ReviewsResponse(
		val statusCode: Int,
		val data: List<Review>?,
		val success: Boolean,
		val message: String
	)

	data class Review(
		val id: Int,
		val rating: Int,
		val comment: String?,
		val userId: String?,
		val productId: Int,
		val createdAt: String?,
		val updatedAt: String?,
		val user: User,
		val images: List<String>?
	)

	data class User(
		val id: String,
		val fullName: String,
		val username: String,
		val email: String,
		val role: String,
		val provider: String,
		val profileImageUrl: String?,
		val createdAt: String,
		val updatedAt: String
	)

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
	val rating: String,
	@SerializedName("comment")
	val comment: String,
	@SerializedName("user_id")
	val userId: String,
	@SerializedName("product_id")
	val productId: String,
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
	val rating: String,
	@SerializedName("comment")
	val comment: String,
	@SerializedName("user_id")
	val userId: String,
	@SerializedName("product_id")
	val productId: String,
	@SerializedName("images")
	val images: List<ReviewImageRequest>
)

data class ReviewImageRequest(
	@SerializedName("url")
	val url: String,
	@SerializedName("type")
	val type: String
)
