package com.capstone.surevenir.model

data class CreateCartResponse(
	val statusCode: Int? = null,
	val data: DataCart? = null,
	val success: Boolean? = null,
	val message: String? = null
)

data class CartItem(
	val createdAt: String? = null,
	val product: Product? = null,
	val quantity: Int? = null,
	val userId: String? = null,
	val productId: Int? = null,
	val id: Int? = null,
	val slug: String? = null,
	val updatedAt: String? = null
)

data class DataCart(
	val totalPrice: Int? = null,
	val cart: List<CartItem?>? = null
)

data class CreateCartRequest(
	val productId: Int,
	val quantity: Int
)