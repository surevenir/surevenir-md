package com.capstone.surevenir.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.surevenir.data.network.response.ReviewData
import com.capstone.surevenir.data.network.response.ReviewImageRequest
import com.capstone.surevenir.data.network.response.ReviewRequest
import com.capstone.surevenir.data.repository.ReviewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(
    private val reviewsRepository: ReviewsRepository
) : ViewModel() {

    private val _reviewsResponse = MutableLiveData<List<ReviewData>>()
    val reviewResponse: LiveData<List<ReviewData>> get() = _reviewsResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    private val _userReviews = MutableStateFlow<Set<Int>>(emptySet())
    val userReviews: StateFlow<Set<Int>> = _userReviews.asStateFlow()

    fun isProductRated(productId: Int): Boolean {
        return _userReviews.value.contains(productId)
    }

    fun postReview(
        token: String,
        rating: Int,
        comment: String,
        userId: String,
        productId: Int,
        images: List<ReviewImageRequest>
    ) {
        viewModelScope.launch {
            try {
                val request = ReviewRequest(
                    rating = rating.toString(),
                    comment = comment,
                    userId = userId,
                    productId = productId.toString(),
                    images = images
                )
                val response = reviewsRepository.postReview(token, request)

                if (response.isSuccessful) {
                    val reviewResponse = response.body()
                    if (reviewResponse?.success == true) {
                        Log.d("ReviewViewModel", "Review posted successfully")
                    } else {
                        // Handle API error
                        _errorMessage.value = reviewResponse?.message ?: "Failed to post review"
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.value = "Error: ${response.code()} - $errorBody"
                    Log.e("ReviewViewModel", "Error posting review: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("ReviewViewModel", "Exception posting review", e)
                _errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun getReviews(productId: Int, token: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = reviewsRepository.getReviews(productId, token)
                val reviewsResponse = response.body()

                if (reviewsResponse?.success == true) {
                    reviewsResponse.data.let { reviews ->
                        _reviewsResponse.value = listOf(reviews)
                    }
                } else {
                    _errorMessage.value = reviewsResponse?.message ?: "API Error"
                }
            } catch (e: Exception) {
                Log.e("ProductDetail Debug", "Exception: ${e.message}")
                _errorMessage.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}