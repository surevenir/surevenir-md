package com.capstone.surevenir.data.repository

import com.capstone.surevenir.data.network.ApiService
import com.capstone.surevenir.data.network.response.FavoriteResponse
import javax.inject.Inject

class FavoriteRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun addToFavorites(productId: Int, token: String): FavoriteResponse {
        return apiService.addToFavorites(productId, "Bearer $token")
    }
}