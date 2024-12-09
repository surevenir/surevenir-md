package com.capstone.surevenir.data.repository

import com.capstone.surevenir.data.network.ApiService
import com.capstone.surevenir.data.network.response.ReviewRequest
import com.capstone.surevenir.data.network.response.ReviewResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewsRepository @Inject constructor(
    private val apiService: ApiService
){

    suspend fun getReviews(productId: Int, token: String): Response<ReviewResponse> {
        return apiService.getReviews(productId, token)
    }

    suspend fun postReview(token: String, reviewRequest: ReviewRequest): Response<ReviewResponse> {
        return apiService.postReview(token, reviewRequest)
    }

}