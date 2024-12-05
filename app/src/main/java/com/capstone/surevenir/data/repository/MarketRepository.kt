package com.capstone.surevenir.data.repository

import com.capstone.surevenir.data.network.ApiService
import com.capstone.surevenir.data.network.response.MarketDetailResponse
import com.capstone.surevenir.data.network.response.MarketResponse
import retrofit2.Response
import javax.inject.Inject

class MarketRepository @Inject constructor(
    private val apiService: ApiService
){

    suspend fun getMarkets(token: String): Response<MarketResponse>{
        return apiService.getMarkets(token)
    }

    suspend fun getMarketDetail(marketId: Int, token: String): MarketDetailResponse {
        return apiService.getMarketDetail(marketId, "Bearer $token")
    }
}