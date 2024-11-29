package com.capstone.surevenir.data.repository

import com.capstone.surevenir.data.network.ApiService
import com.capstone.surevenir.data.network.response.ProductDetailResponse
import com.capstone.surevenir.data.network.response.ProductResponse
import com.capstone.surevenir.model.Product
import retrofit2.Response
import javax.inject.Inject


class ProductRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getProducts(token: String): Response<ProductResponse> {
        return apiService.getProducts(token)
    }

    suspend fun getProductDetail(productId: Int, token: String): ProductDetailResponse {
        return apiService.getProductDetail(productId, "Bearer $token")
    }

}
