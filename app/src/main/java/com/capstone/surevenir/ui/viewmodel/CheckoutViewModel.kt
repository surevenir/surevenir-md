package com.capstone.surevenir.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.surevenir.data.network.response.CartItem
import com.capstone.surevenir.data.network.response.CheckoutData
import com.capstone.surevenir.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _checkoutData = MutableStateFlow<List<CheckoutData>>(emptyList())
    val checkoutData: StateFlow<List<CheckoutData>> = _checkoutData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _checkoutSuccess = MutableStateFlow(false)
    val checkoutSuccess: StateFlow<Boolean> = _checkoutSuccess

    fun getCheckouts(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = cartRepository.getCheckouts(token)
                if (response.isSuccessful && response.body() != null) {
                    _checkoutData.value = response.body()!!.data
                } else {
                    _error.value = "Failed to fetch checkouts: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun checkout(token: String, cartItems: List<CartItem>) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = cartRepository.checkout(token, cartItems)
                if (response.isSuccessful) {
                    _checkoutSuccess.value = true
                    getCheckouts(token)
                } else {
                    _error.value = "Checkout failed: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Error during checkout: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetCheckoutSuccess() {
        _checkoutSuccess.value = false
    }

    fun clearError() {
        _error.value = null
    }
}