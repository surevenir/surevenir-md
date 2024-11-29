package com.capstone.surevenir.data.network.response

import com.google.gson.annotations.SerializedName

data class MerchantDetailResponse(
	val success: Boolean,
	val status_code: Int? = null,
	val data: MerchantDetail? = null,
	val message: String? = null
)

data class MerchantDetail(
	@SerializedName("id")
	val id: Int,
	@SerializedName("slug")
	val slug: String,
	@SerializedName("name")
	val name: String,
	@SerializedName("profile_image_url")
	val profileImageUrl: String?,
	@SerializedName("description")
	val description: String,
	@SerializedName("addresses")
	val addresses: Any?, // Jika null atau tipe data tidak jelas, gunakan `Any?`
	@SerializedName("longitude")
	val longitude: String,
	@SerializedName("latitude")
	val latitude: String,
	@SerializedName("user_id")
	val userId: String,
	@SerializedName("market_id")
	val marketId: Int,
	@SerializedName("createdAt")
	val createdAt: String,
	@SerializedName("updatedAt")
	val updatedAt: String
)
