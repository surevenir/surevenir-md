package com.capstone.surevenir.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.surevenir.data.network.response.FavoriteResponse
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
}