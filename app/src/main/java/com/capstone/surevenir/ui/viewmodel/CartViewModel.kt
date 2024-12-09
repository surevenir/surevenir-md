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

    private val _isUpdating = MutableStateFlow(false)

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

    fun deleteCartItem(token: String, cartItemId: Int) {
        viewModelScope.launch {
            try {
                val response = cartRepository.deleteCartItem(token, cartItemId)
                if (response.isSuccessful) {
                    getCart(token)
                } else {
                    _errorMessage.value = "Failed to delete item: ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun updateCartItemQuantity(token: String, cartItemId: Int, newQuantity: Int) {
        Log.d("CartViewModel", "updateCartItemQuantity started for ID: $cartItemId with quantity: $newQuantity")

        if (_isUpdating.value) {
            Log.d("CartViewModel", "Update already in progress, skipping")
            return
        }

        viewModelScope.launch {
            _isUpdating.value = true
            _isLoading.value = true
            _errorMessage.value = null

            try {
                Log.d("CartViewModel", "Calling repository.updateCartItemQuantity")
                val response = cartRepository.updateCartItemQuantity(token, cartItemId, newQuantity)

                Log.d("CartViewModel", "Update response received: ${response.isSuccessful}")

                if (response.isSuccessful) {
                    Log.d("CartViewModel", "Update successful, refreshing cart")
                    getCart(token)
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("CartViewModel", "Update failed with error: $errorBody")
                    _errorMessage.value = "Failed to update item quantity: ${response.message()}"
                }
            } catch (e: Exception) {
                Log.e("CartViewModel", "Exception during update", e)
                _errorMessage.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
                _isUpdating.value = false
                Log.d("CartViewModel", "Update operation completed")
            }
        }
    }
}