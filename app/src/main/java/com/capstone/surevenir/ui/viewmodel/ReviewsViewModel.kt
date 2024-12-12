package com.capstone.surevenir.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.surevenir.data.network.response.CategoryData
import com.capstone.surevenir.data.network.response.CategoryResponse
import com.capstone.surevenir.data.network.response.Review
import com.capstone.surevenir.data.network.response.ReviewImageRequest
import com.capstone.surevenir.data.network.response.ReviewRequest
import com.capstone.surevenir.data.network.response.ReviewsResponse
import com.capstone.surevenir.data.repository.ReviewsRepository
import com.capstone.surevenir.model.Category
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

    private val _reviewsResponse = MutableLiveData<List<Review>>()
    val reviewResponse: LiveData<List<Review>> get() = _reviewsResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    private val _averageRating = MutableLiveData<Double>()
    val averageRating: LiveData<Double> get() = _averageRating

    private val _totalReviews = MutableLiveData<Int>()
    val totalReviews: LiveData<Int> get() = _totalReviews

    private val _userReviews = MutableStateFlow<Set<Int>>(emptySet())
    val userReviews: StateFlow<Set<Int>> = _userReviews.asStateFlow()

    private val _reviewState = MutableLiveData<ReviewState>(ReviewState.Initial)
    val reviewState: LiveData<ReviewState> = _reviewState

    fun isProductRated(productId: Int): Boolean {
        return _userReviews.value.contains(productId)
    }

    fun postReview(
        token: String,
        rating: Int,
        comment: String,
        productId: Int,
        imageUris: List<String>
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                Log.d("ReviewsViewModel", "Posting review with token: $token")

                val response = reviewsRepository.postReview(
                    token = token,
                    rating = rating,
                    comment = comment,
                    productId = productId,
                    imageUris = imageUris
                )

                if (response.isSuccessful && response.body()?.success == true) {
                    _userReviews.value += productId
                    _reviewState.value = ReviewState.Success
                } else {
                    val errorMessage = if (response.errorBody()?.string()?.contains("already reviewed") == true) {
                        _userReviews.value += productId
                        "You have already reviewed this product"
                    } else {
                        response.errorBody()?.string() ?: "Unknown error occurred"
                    }
                    _reviewState.value = ReviewState.Error(errorMessage)
                }
            } catch (e: Exception) {
                _reviewState.value = ReviewState.Error(e.message ?: "Unknown error occurred")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getReviews(productId: Int, token: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = reviewsRepository.getReviews(productId, token)
                val reviewsResponse = response.body()

                if (reviewsResponse?.success == true && reviewsResponse.data != null) {
                    Log.d("ProductDetail Debug", "Reviews fetched successfully: ${reviewsResponse.data}")
                    val reviews = reviewsResponse.data.map { review ->
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

                    _reviewsResponse.value = reviews
                    _totalReviews.value = reviews.size

                    val totalRating = reviews.sumOf { it.rating }
                    _averageRating.value = if (reviews.isNotEmpty()) {
                        totalRating.toDouble() / reviews.size
                    } else {
                        0.0
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

    fun resetReviewState() {
        _reviewState.value = ReviewState.Initial
    }

    sealed class ReviewState {
        data object Initial : ReviewState()
        data object Success : ReviewState()
        data class Error(val message: String) : ReviewState()
    }
}
