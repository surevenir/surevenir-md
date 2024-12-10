package com.capstone.surevenir.data.network.response

import com.google.gson.annotations.SerializedName

data class CategoryDetailResponse(
	val statusCode: Int? = null,
	val data: CategoryData? = null,
	val success: Boolean? = null,
	val message: String? = null
)

data class CategoryData(
	val createdAt: String? = null,
	val name: String? = null,
	val description: String? = null,
	@SerializedName("range_price")
	val rangePrice: String? = null,
	@SerializedName("image_url")
	val image_url: Any? = null,
	val id: Int? = null,
	val updatedAt: String? = null
)

data class CategoryDataProduct(
	val id: Int,
	val name: String
)

