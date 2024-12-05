package com.capstone.surevenir.data.repository

import android.util.Log
import com.capstone.surevenir.data.network.ApiService
import com.capstone.surevenir.data.network.response.CartResponse
import com.capstone.surevenir.data.network.response.CheckoutRequest
import com.capstone.surevenir.data.network.response.CheckoutResponse
import com.capstone.surevenir.data.network.response.DeleteCartResponse
import com.capstone.surevenir.model.CreateCartRequest
import com.capstone.surevenir.model.CreateCartResponse
import retrofit2.Response
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun createCart(token: String, request: CreateCartRequest): Response<CreateCartResponse> {
        return try {
            Log.d("CartRepository", "Creating cart with product_id: ${request.productId}, quantity: ${request.quantity}")
            val response = apiService.createCarts(token, request)
            Log.d("CartRepository", "Create cart API response: ${response.code()}")
            response
        } catch (e: Exception) {
            Log.e("CartRepository", "Error in createCart", e)
            throw Exception("Failed to create cart: ${e.message}")
        }
    }

    suspend fun getCart(token: String): Response<CartResponse> {
        val response = apiService.getCart(token)
        return response
    }

    suspend fun deleteCartItem(token: String, cartItemId: Int): Response<DeleteCartResponse> {
        Log.d("CartRepository", "Starting delete request for item $cartItemId")
        return try {
            Log.d("CartRepository", "Making API call...")
            val response = apiService.deleteCartItem(cartItemId, token)
            Log.d("CartRepository", "API call completed with code: ${response.code()}")
            response
        } catch (e: Exception) {
            Log.e("CartRepository", "Error in deleteCartItem", e)
            throw Exception("Failed to delete cart item: ${e.message}")
        }
    }

    suspend fun checkout(token: String, cartItemIds: List<Int>): Response<CheckoutResponse> {
        Log.d("CartRepository", "Starting checkout for items: $cartItemIds")
        return try {
            Log.d("CartRepository", "Making checkout API call...")
            val request = CheckoutRequest(cartItemIds = cartItemIds)
            val response = apiService.checkout(token, request)
            Log.d("CartRepository", "Checkout API call completed with code: ${response.code()}")
            response
        } catch (e: Exception) {
            Log.e("CartRepository", "Error in checkout", e)
            throw Exception("Failed to checkout: ${e.message}")
        }
    }

    suspend fun getCheckouts(token: String): Response<CheckoutResponse> {
        Log.d("CartRepository", "Fetching checkouts")
        return try {
            Log.d("CartRepository", "Making get checkouts API call...")
            val response = apiService.getCheckouts(token)
            Log.d("CartRepository", "Get checkouts API call completed with code: ${response.code()}")
            response
        } catch (e: Exception) {
            Log.e("CartRepository", "Error in getCheckouts", e)
            throw Exception("Failed to get checkouts: ${e.message}")
        }
    }
}
