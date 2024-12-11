package com.capstone.surevenir.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.surevenir.data.network.response.ReviewData
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

    fun resetReviewState() {
        _reviewState.value = ReviewState.Initial
    }

    sealed class ReviewState {
        data object Initial : ReviewState()
        data object Success : ReviewState()
        data class Error(val message: String) : ReviewState()
    }
}