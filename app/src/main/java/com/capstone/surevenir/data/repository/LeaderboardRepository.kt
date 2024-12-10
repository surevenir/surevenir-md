package com.capstone.surevenir.data.repository

import com.capstone.surevenir.data.network.ApiService
import com.capstone.surevenir.data.network.response.LeaderboardResponse
import javax.inject.Inject

class LeaderboardRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getLeaderboard(token: String): Result<LeaderboardResponse> = try {
        val response = apiService.getLeaderboard("Bearer $token")
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }
}