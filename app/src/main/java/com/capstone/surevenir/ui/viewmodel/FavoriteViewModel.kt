package com.capstone.surevenir.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.surevenir.data.network.response.DeleteFavoriteResponse
import com.capstone.surevenir.data.network.response.FavoriteProductItem
import com.capstone.surevenir.data.network.response.FavoriteResponse
import com.capstone.surevenir.data.network.response.ProductFavorite
import com.capstone.surevenir.data.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _addFavoriteResult = MutableStateFlow<Result<FavoriteResponse>?>(null)
    val addFavoriteResult: StateFlow<Result<FavoriteResponse>?> = _addFavoriteResult

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _favoriteProducts = MutableStateFlow<List<FavoriteProductItem>>(emptyList())
    val favoriteProducts: StateFlow<List<FavoriteProductItem>> = _favoriteProducts


    private val _isLoadingProducts = MutableStateFlow(false)
    val isLoadingProducts: StateFlow<Boolean> = _isLoadingProducts

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun addToFavorites(token: String, productId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = favoriteRepository.addToFavorites(productId, token)
                _addFavoriteResult.value = Result.success(response)
            } catch (e: Exception) {
                _addFavoriteResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun getFavoriteProducts(token: String) {
        viewModelScope.launch {
            _isLoadingProducts.value = true
            try {
                val response = favoriteRepository.getFavoriteProducts(token)
                if (response.success) {
                    _favoriteProducts.value = response.data.products
                    Log.d("FavoriteViewModel", "Products fetched: ${response.data.products.size}")
                } else {
                    _error.value = response.message
                    Log.e("FavoriteViewModel", "Error: ${response.message}")
                }
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("FavoriteViewModel", "Exception: ${e.message}")
            } finally {
                _isLoadingProducts.value = false
            }
        }
    }

    private val _deleteFavoriteResult = MutableStateFlow<Result<DeleteFavoriteResponse>?>(null)
    val deleteFavoriteResult: StateFlow<Result<DeleteFavoriteResponse>?> = _deleteFavoriteResult


    fun deleteFavorite(token: String, productId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = favoriteRepository.deleteFavorite(productId, token)
                _deleteFavoriteResult.value = Result.success(response)
            } catch (e: Exception) {
                _deleteFavoriteResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun getProductImage(productId: Int): LiveData<String?> {
        return favoriteRepository.getProductImageById(productId)
    }
}