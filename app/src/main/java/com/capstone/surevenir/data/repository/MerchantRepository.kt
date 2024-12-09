package com.capstone.surevenir.data.repository

import com.capstone.surevenir.data.dao.MerchantDao
import com.capstone.surevenir.data.entity.MerchantDatabase
import com.capstone.surevenir.data.network.ApiService
import com.capstone.surevenir.data.network.response.MerchantData
import com.capstone.surevenir.data.network.response.MerchantDetailResponse
import com.capstone.surevenir.data.network.response.MerchantResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class MerchantRepository @Inject constructor(
    private val apiService: ApiService,
    private val merchantDao: MerchantDao
) {
    // Remote data
    suspend fun fetchMerchantsFromApi(token: String): Response<MerchantResponse> {
        return apiService.getMerchants(token)
    }

    suspend fun getMerchantDetail(merchantId: Int, token: String): MerchantDetailResponse {
        return apiService.getMerchantDetail(merchantId, "Bearer $token")
    }

    // Local data
    fun getAllMerchants(): Flow<List<MerchantDatabase>> {
        return merchantDao.getAllMerchants()
    }


    suspend fun updateLocalMerchants(merchants: List<MerchantDatabase>) {
        withContext(Dispatchers.IO) {  // Tambahkan ini
            merchantDao.updateMerchants(merchants)

        }
    }

    fun getMerchantsByMarketId(marketId: Int): Flow<List<MerchantDatabase>> {
        return merchantDao.getMerchantsByMarketId(marketId)
    }

    // Function to fetch and cache merchants
    suspend fun refreshMerchants(token: String) {
        withContext(Dispatchers.IO) {  // Tambahkan ini
            try {
                val response = fetchMerchantsFromApi(token)
                if (response.isSuccessful) {
                    response.body()?.data?.map { it.toMerchantDatabase() }?.let { merchants ->
                        insertMerchants(merchants)
                    }
                }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    private suspend fun insertMerchants(merchants: List<MerchantDatabase>) {
        withContext(Dispatchers.IO) {  // Tambahkan ini
            merchantDao.insertMerchants(merchants)
        }
    }


    // Function to map API data to database entity
    private fun MerchantData.toMerchantDatabase() = MerchantDatabase(
        id = id,
        name = name,
        profileImageUrl = profile_image_url,
        description = description,
        longitude = longitude,
        latitude = latitude,
        userId = user_id,
        marketId = market_id,
        createdAt = createdAt,
        updatedAt = updatedAt,
        productsCount = product_count,
        location = location
    )

}