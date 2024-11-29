package com.capstone.surevenir.data.repository

import com.capstone.surevenir.data.network.ApiService
import com.capstone.surevenir.data.network.response.MerchantDetailResponse
import com.capstone.surevenir.data.network.response.MerchantResponse
import retrofit2.Response
import javax.inject.Inject

class MerchantRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getMerchants(token: String): Response<MerchantResponse> {
        return apiService.getMerchants(token)
    }

    suspend fun getMerchantDetail(merchantId: Int, token: String): MerchantDetailResponse {
        return apiService.getMerchantDetail(merchantId, "Bearer $token")
    }

}
