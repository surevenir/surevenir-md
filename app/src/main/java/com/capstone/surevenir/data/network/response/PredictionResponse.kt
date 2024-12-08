package com.capstone.surevenir.data.network.response

import com.google.gson.annotations.SerializedName

data class PredictionResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("status_code") val statusCode: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: PredictionData
)

data class PredictionData(
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("prediction") val prediction: Prediction,
    @SerializedName("category") val category: CategoryPrediction,
    @SerializedName("related_products") val relatedProducts: List<RelatedProduct>
)

data class Prediction(
    @SerializedName("accuration") val accuration: Double,
    @SerializedName("result") val result: String
)

data class CategoryPrediction(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("range_price") val rangePrice: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)

data class RelatedProduct(
    @SerializedName("id") val id: Int,
    @SerializedName("slug") val slug: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val price: Int,
    @SerializedName("merchant_id") val merchantId: Int,
    @SerializedName("stock") val stock: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("merchant") val merchant: MerchantData,
    @SerializedName("images") val images: List<String>
)