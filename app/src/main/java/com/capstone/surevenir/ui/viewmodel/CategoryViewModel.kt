package com.capstone.surevenir.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airbnb.lottie.parser.moshi.JsonReader.Token
import com.capstone.surevenir.data.network.response.CategoryData
import com.capstone.surevenir.data.network.response.CategoryResponse
import com.capstone.surevenir.data.network.response.ProductDetail
import com.capstone.surevenir.data.network.response.UserResponse
import com.capstone.surevenir.data.repository.CategoryRepository
import com.capstone.surevenir.model.Category
import com.capstone.surevenir.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
): ViewModel(){

    private val _categoryResponse = MutableLiveData<CategoryResponse>()
    val categoryResponse: LiveData<CategoryResponse?> get() = _categoryResponse

    private val _categoryDetail = MutableStateFlow<CategoryData?>(null)
    val categoryDetail: StateFlow<CategoryData?> = _categoryDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage



    fun getCategories(token: String, onComplete: (List<Category>?) -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = categoryRepository.getCategories(token)
                _isLoading.value = false
                if (response.isSuccessful) {
                    val categoryList = response.body()?.data
                    _categoryResponse.value = response.body()
                    onComplete(categoryList)
                } else {
                    _errorMessage.value = "Error: ${response.code()} - ${response.message()}"
                    Log.e("CategoryViewModel", "API Error: ${response.message()}")
                    onComplete(null)
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Failed: ${e.message}"
                Log.e("CategoryViewModel", "Exception: ${e.message}")
                onComplete(null)
            }
        }
    }

    fun getCategoryDetail(productId: Int, token: String) {
        viewModelScope.launch {
            Log.d(
                "ProductDetail Debug",
                "Fetching product detail for ID: $productId with token: $token"
            )
            try {
                val response = categoryRepository.getCategoryDetail(productId, token)
                if (response.success == true) {
                    Log.d(
                        "ProductDetail Debug",
                        "Product detail fetched successfully: ${response.data}"
                    )
                    _categoryDetail.value = response.data
                } else {
                    Log.e("ProductDetail Debug", "API responded with error: ${response.message}")
                    _errorMessage.value = response.message
                }
            } catch (e: Exception) {
                Log.e("ProductDetail Debug", "Exception occurred: ${e.message}")
                _errorMessage.value = e.message
            }
        }
    }
}