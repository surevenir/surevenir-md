package com.capstone.surevenir.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.surevenir.data.network.response.MarketResponse
import com.capstone.surevenir.data.network.response.ProductData
import com.capstone.surevenir.data.network.response.ProductResponse
import com.capstone.surevenir.data.repository.ProductRepository
import com.capstone.surevenir.model.Market
import com.capstone.surevenir.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _productResponse = MutableLiveData<ProductResponse>()
    val productsResponse: LiveData<ProductResponse?> get() = _productResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun getProducts(token: String, onComplete: (List<ProductData>?) -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = productRepository.getProducts(token)
                _isLoading.value = false
                if (response.isSuccessful) {
                    val marketList = response.body()?.data
                    _productResponse.value = response.body()
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
}
