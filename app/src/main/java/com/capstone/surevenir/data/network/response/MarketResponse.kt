package com.capstone.surevenir.data.network.response

import com.capstone.surevenir.model.Market

data class MarketResponse(
	val statusCode: Int? = null,
	val data: List<Market>? = null,
	val success: Boolean? = null,
	val message: String? = null
)

data class DataMarket(
	val createdAt: String? = null,
	val latitude: String? = null,
	val name: String? = null,
	val description: String? = null,
	val profileImageUrl: Any? = null,
	val id: Int? = null,
	val slug: String? = null,
	val longitude: String? = null,
	val updatedAt: String? = null
)

