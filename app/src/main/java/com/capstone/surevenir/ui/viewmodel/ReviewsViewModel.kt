package com.capstone.surevenir.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.surevenir.data.network.response.CategoryData
import com.capstone.surevenir.data.network.response.CategoryResponse
import com.capstone.surevenir.data.network.response.Review
import com.capstone.surevenir.data.network.response.ReviewsResponse
import com.capstone.surevenir.data.repository.ReviewsRepository
import com.capstone.surevenir.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(
    private val reviewsRepository: ReviewsRepository
) : ViewModel() {

    private val _reviewsResponse = MutableLiveData<List<Review>>()
    val reviewResponse: LiveData<List<Review>> get() = _reviewsResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    fun getReviews(productId: Int, token: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = reviewsRepository.getReviews(productId, token)
                val reviewsResponse = response.body()

                if (reviewsResponse?.success == true && reviewsResponse.data != null) {
                    Log.d("ProductDetail Debug", "Reviews fetched successfully: ${reviewsResponse.data}")
                    _reviewsResponse.value = reviewsResponse.data.map { review ->
                        Review(
                            id = review.id,
                            rating = review.rating,
                            comment = review.comment,
                            userId = review.userId,
                            productId = review.productId,
                            createdAt = review.createdAt,
                            updatedAt = review.updatedAt,
                            user = review.user,
                            images = review.images
                        )
                    }
                } else {
                    Log.e("ProductDetail Debug", "API responded with error: ${reviewsResponse?.message}")
                    _errorMessage.value = reviewsResponse?.message ?: "Unknown error occurred"
                }
            } catch (e: Exception) {
                Log.e("ProductDetail Debug", "Exception occurred: ${e.message}")
                _errorMessage.value = "An error occurred: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}