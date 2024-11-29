package com.capstone.surevenir.data.network.response

import com.capstone.surevenir.model.Category

data class CategoryResponse(
	val statusCode: Int? = null,
	val data: List<Category>? = null,
	val success: Boolean? = null,
	val message: String? = null
)

data class DataCategory(
	val createdAt: String? = null,
	val name: String? = null,
	val id: Int? = null,
	val updatedAt: String? = null
)

