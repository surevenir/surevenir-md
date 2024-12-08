package com.capstone.surevenir.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.surevenir.data.network.response.CartItem
import com.capstone.surevenir.data.network.response.CheckoutData
import com.capstone.surevenir.data.repository.CheckoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val checkoutRepository: CheckoutRepository
) : ViewModel() {

    private val _checkoutData = MutableStateFlow<List<CheckoutData>>(emptyList())
    val checkoutData: StateFlow<List<CheckoutData>> = _checkoutData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _checkoutSuccess = MutableStateFlow(false)
    val checkoutSuccess: StateFlow<Boolean> = _checkoutSuccess.asStateFlow()

    fun getCheckouts(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = checkoutRepository.getCheckouts(token)
                if (response.isSuccessful && response.body() != null) {
                    _checkoutData.value = response.body()!!.data
                } else {
                    _error.value = "Failed to fetch checkouts: ${response.message()}"
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching checkouts", e)
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun checkout(token: String, selectedItems: List<CartItem>) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                Log.d(TAG, "Starting checkout process with ${selectedItems.size} items")
                val response = checkoutRepository.checkout(token, selectedItems)
                Log.d(TAG, "Checkout response: ${response.code()}")

                if (response.isSuccessful) {
                    response.body()?.let { checkoutResponse ->
                        Log.d(TAG, "Checkout successful: ${checkoutResponse.message}")
                        _checkoutSuccess.value = true
                    } ?: run {
                        _error.value = "Response body is null"
                    }
                } else {
                    Log.e(TAG, "Checkout failed: ${response.errorBody()?.string()}")
                    _error.value = "Checkout failed: ${response.message()}"
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error during checkout", e)
                _error.value = "Error: ${e.message}"
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

    companion object {
        private const val TAG = "CheckoutViewModel"
    }
}