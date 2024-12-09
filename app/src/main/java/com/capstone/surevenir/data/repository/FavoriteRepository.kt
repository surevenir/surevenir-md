package com.capstone.surevenir.data.repository

import android.util.Log
import com.capstone.surevenir.data.network.ApiService
import com.capstone.surevenir.data.network.response.DeleteFavoriteResponse
import com.capstone.surevenir.data.network.response.FavoriteResponse
import com.capstone.surevenir.data.network.response.GetFavoriteProductsResponse
import javax.inject.Inject

class FavoriteRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun addToFavorites(productId: Int, token: String): FavoriteResponse {
        return apiService.addToFavorites(productId, "Bearer $token")
    }

    suspend fun getFavoriteProducts(token: String): GetFavoriteProductsResponse {
        return apiService.getFavoriteProducts("Bearer $token")
    }

    suspend fun deleteFavorite(productId: Int, token: String): DeleteFavoriteResponse {
        return apiService.deleteFavorite(productId, "Bearer $token")
    }
}