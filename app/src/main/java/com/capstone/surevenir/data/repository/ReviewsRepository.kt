package com.capstone.surevenir.data.repository

import com.capstone.surevenir.data.network.ApiService
import com.capstone.surevenir.data.network.response.CategoryResponse
import com.capstone.surevenir.data.network.response.ReviewRequest
import com.capstone.surevenir.data.network.response.ReviewResponse
import com.capstone.surevenir.data.network.response.ReviewsResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewsRepository @Inject constructor(
    private val apiService: ApiService
){

    suspend fun getReviews(productId: Int, token: String): Response<ReviewsResponse> {
        return apiService.getReviews(productId, token)
    }

    suspend fun postReview(token: String, reviewRequest: ReviewRequest): Response<ReviewResponse> {
        return apiService.postReview(token, reviewRequest)
    }

}