package com.capstone.surevenir.data.network.response

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

