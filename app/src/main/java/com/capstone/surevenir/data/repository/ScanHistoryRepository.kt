package com.capstone.surevenir.data.repository

import com.capstone.surevenir.data.network.ApiService
import com.capstone.surevenir.data.network.response.ScanHistory
import javax.inject.Inject


class ScanHistoryRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getUserScanHistory(token: String): Result<List<ScanHistory>> = try {
        val response = apiService.getUserScanHistory("Bearer $token")
        if (response.success) {
            Result.success(response.data)
        } else {
            Result.failure(Exception(response.message))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
