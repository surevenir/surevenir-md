package com.capstone.surevenir.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.surevenir.data.network.response.CategoryResponse
import com.capstone.surevenir.data.network.response.MarketDetail
import com.capstone.surevenir.data.network.response.MarketResponse
import com.capstone.surevenir.data.repository.MarketRepository
import com.capstone.surevenir.model.Category
import com.capstone.surevenir.model.Market
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val marketRepository: MarketRepository
): ViewModel() {
    private val _marketResponse = MutableLiveData<MarketResponse>()
    val marketResponse: LiveData<MarketResponse?> get() = _marketResponse

    private val _marketDetail = MutableStateFlow<MarketDetail?>(null)
    val marketDetail: StateFlow<MarketDetail?> = _marketDetail.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun getMarkets(token: String, onComplete: (List<Market>?) -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = marketRepository.getMarkets(token)
                _isLoading.value = false
                if (response.isSuccessful) {
                    val marketList = response.body()?.data
                    _marketResponse.value = response.body()
                    onComplete(marketList)
                } else {
                    _errorMessage.value = "Error: ${response.code()} - ${response.message()}"
                    Log.e("MarketViewModel", "API Error: ${response.message()}")
                    onComplete(null)
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Failed: ${e.message}"
                Log.e("MarketViewModel", "Exception: ${e.message}")
                onComplete(null)
            }
        }
    }

    fun fetchMarketDetail(marketId: Int, token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = marketRepository.getMarketDetail(marketId, token)
                if (response.success) {
                    _marketDetail.value = response.data
                    _error.value = null
                } else {
                    _error.value = response.message
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "An unexpected error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
}