package com.capstone.surevenir.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.surevenir.data.network.response.ProductDetail
import com.capstone.surevenir.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _productDetail = MutableStateFlow<ProductDetail?>(null)
    val productDetail: StateFlow<ProductDetail?> = _productDetail

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchProductDetail(productId: Int, token: String) {
        viewModelScope.launch {
            Log.d("ProductDetail Debug", "Fetching product detail for ID: $productId with token: $token")
            try {
                val response = repository.getProductDetail(productId, token)
                if (response.success) {
                    Log.d("ProductDetail Debug", "Product detail fetched successfully: ${response.data}")
                    _productDetail.value = response.data
                } else {
                    Log.e("ProductDetail Debug", "API responded with error: ${response.message}")
                    _error.value = response.message
                }
            } catch (e: Exception) {
                Log.e("ProductDetail Debug", "Exception occurred: ${e.message}")
                _error.value = e.message
            }
        }
    }

}
