package com.capstone.surevenir.data.repository

import com.capstone.surevenir.data.network.ApiService
import com.capstone.surevenir.data.network.response.CategoryResponse
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class CategoryRepository  @Inject constructor(
    private val apiService: ApiService
){

    suspend fun getCategories(token: String): Response<CategoryResponse> {
        return apiService.getCategories(token)
    }

}