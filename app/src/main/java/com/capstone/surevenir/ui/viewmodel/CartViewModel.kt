package com.capstone.surevenir.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.surevenir.data.network.response.CartData
import com.capstone.surevenir.data.repository.CartRepository
import com.capstone.surevenir.model.CreateCartRequest
import com.capstone.surevenir.model.CreateCartResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _cartData = MutableStateFlow<CartData?>(null)
    val cartData: StateFlow<CartData?> get() = _cartData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    private val _createCartResult = MutableStateFlow<Result<CreateCartResponse>?>(null)
    val createCartResult: StateFlow<Result<CreateCartResponse>?> = _createCartResult

    fun createCart(token: String, productId: Int, quantity: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val request = CreateCartRequest(productId = productId, quantity = quantity)
                val response = cartRepository.createCart("Bearer $token", request)

                if (response.isSuccessful) {
                    _createCartResult.value = Result.success(response.body()!!)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("CartViewModel", "Error response: $errorBody")
                    _createCartResult.value = Result.failure(Exception(errorBody))
                }
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error creating cart", e)
                _createCartResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun getCart(token: String) {
        Log.d("CartViewModel", "getCart called with token: $token")
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val response = cartRepository.getCart(token)
                if (response.isSuccessful) {
                    response.body()?.data?.let {
                        _cartData.value = it
                    } ?: run {
                        _errorMessage.value = "Cart data is empty"
                    }
                } else {
                    _errorMessage.value = "Error: ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private val _isDeleting = MutableStateFlow(false)
    val isDeleting: StateFlow<Boolean> = _isDeleting

    fun deleteCartItem(token: String, cartItemId: Int) {
        Log.d("CartViewModel", "deleteCartItem started for ID: $cartItemId")

        if (_isDeleting.value) {
            Log.d("CartViewModel", "Delete already in progress, skipping")
            return
        }

        viewModelScope.launch {
            _isDeleting.value = true
            _isLoading.value = true
            _errorMessage.value = null

            try {
                Log.d("CartViewModel", "Calling repository.deleteCartItem")
                val response = cartRepository.deleteCartItem(token, cartItemId)

                Log.d("CartViewModel", "Delete response received: ${response.isSuccessful}")

                if (response.isSuccessful) {
                    Log.d("CartViewModel", "Delete successful, refreshing cart")
                    getCart(token) // Refresh cart after successful deletion
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("CartViewModel", "Delete failed with error: $errorBody")
                    _errorMessage.value = "Failed to delete item: ${response.message()}"
                }
            } catch (e: Exception) {
                Log.e("CartViewModel", "Exception during delete", e)
                _errorMessage.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
                _isDeleting.value = false
                Log.d("CartViewModel", "Delete operation completed")
            }
        }
    }
}