package com.capstone.surevenir.data.repository

import android.util.Log
import com.capstone.surevenir.data.network.ApiService
import com.capstone.surevenir.data.network.response.CartItem
import com.capstone.surevenir.data.network.response.CheckoutGetResponse
import com.capstone.surevenir.data.network.response.CheckoutPostResponse
import com.capstone.surevenir.data.network.response.CheckoutRequest
import retrofit2.Response
import javax.inject.Inject

class CheckoutRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun checkout(token: String, cartItems: List<CartItem>): Response<CheckoutPostResponse> {
        if (cartItems.isEmpty()) {
            Log.e(TAG, "Attempted checkout with empty cart items")
            throw IllegalArgumentException("Cart items cannot be empty")
        }

        val cartItemIds = cartItems.map { it.id }
        val productIds = cartItems.map { it.product.id }

        Log.d(TAG, "Cart Item IDs: $cartItemIds")
        Log.d(TAG, "Product IDs: $productIds")

        val request = CheckoutRequest(
            cartItemIds = cartItemIds,
            productIds = productIds
        )
        return apiService.checkout(token, request)
    }

    suspend fun getCheckouts(token: String): Response<CheckoutGetResponse> {
        Log.d(TAG, "Fetching checkouts")
        return try {
            Log.d(TAG, "Making get checkouts API call...")
            val response = apiService.getCheckouts(token)
            Log.d(TAG, "Get checkouts API call completed with code: ${response.code()}")
            response
        } catch (e: Exception) {
            Log.e(TAG, "Error in getCheckouts", e)
            throw Exception("Failed to get checkouts: ${e.message}")
        }
    }

    companion object {
        private const val TAG = "CheckoutRepository"
    }
}