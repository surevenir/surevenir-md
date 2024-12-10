package com.capstone.surevenir.data.network

import com.capstone.surevenir.data.network.response.AllUserResponse
import com.capstone.surevenir.data.network.response.CartResponse
import com.capstone.surevenir.data.network.response.CategoryDetailResponse
import com.capstone.surevenir.data.network.response.CategoryResponse
import com.capstone.surevenir.data.network.response.CheckoutGetResponse
import com.capstone.surevenir.data.network.response.CheckoutPostResponse
import com.capstone.surevenir.data.network.response.CheckoutRequest
import com.capstone.surevenir.data.network.response.CreateUserRequest
import com.capstone.surevenir.data.network.response.DeleteCartResponse
import com.capstone.surevenir.data.network.response.MarketResponse
import com.capstone.surevenir.data.network.response.MerchantDetailResponse
import com.capstone.surevenir.data.network.response.MerchantResponse
import com.capstone.surevenir.data.network.response.PredictionResponse
import com.capstone.surevenir.data.network.response.ProductDetailResponse
import com.capstone.surevenir.data.network.response.ProductResponse
import com.capstone.surevenir.data.network.response.ReviewRequest
import com.capstone.surevenir.data.network.response.ReviewResponse
import com.capstone.surevenir.data.network.response.UserResponse
import com.capstone.surevenir.model.CreateCartRequest
import com.capstone.surevenir.model.CreateCartResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path


interface ApiService {

    @GET("users")
    suspend fun getUsers(@Header("Authorization") token: String): Response<AllUserResponse>
    @POST("users")
    fun createUser(@Body request: CreateUserRequest): Call<UserResponse>

    @GET("categories")
    suspend fun getCategories(@Header("Authorization") token: String): Response<CategoryResponse>

    @GET("merchants")
    suspend fun getMerchants(@Header("Authorization") token: String): Response<MerchantResponse>

    @GET("markets")
    suspend fun getMarkets(@Header("Authorization") token: String): Response<MarketResponse>

    @GET("products/{id}/reviews")
    suspend fun getReviews(
        @Path("id") productId: Int,
        @Header("Authorization") token: String
    ): Response<ReviewResponse>

    @GET("users/{id}")
    suspend fun getUserById(
        @Path("id") userId: String,
        @Header("Authorization") token: String
    ): Response<UserResponse>

    @Multipart
    @PATCH("users/{id}")
    suspend fun updateUser(
        @Path("id") userId: String,
        @Header("Authorization") token: String,
        @Part("full_name") fullName: RequestBody,
        @Part("username") username: RequestBody,
        @Part("phone") phone: RequestBody?,
        @Part("address") address: RequestBody?,
        @Part image: MultipartBody.Part?
    ): Response<UserResponse>

    @POST("carts")
    suspend fun createCarts(
        @Header("Authorization") token: String,
        @Body request: CreateCartRequest
    ): Response<CreateCartResponse>

    @GET("carts")
    suspend fun getCart(
        @Header("Authorization") token: String
    ): Response<CartResponse>

    @DELETE("carts/{id}")
    suspend fun deleteCartItem(
        @Path("id") cartItemId: Int,
        @Header("Authorization") token: String
    ): Response<DeleteCartResponse>

    @PATCH("carts/{id}")
    suspend fun updateCartQuantity(
        @Path("id") cartItemId: Int,
        @Header("Authorization") token: String,
        @Body quantity: Map<String, Int>
    ): Response<CartResponse>

    @Multipart
    @POST("carts/checkout")
    suspend fun checkout(
        @Header("Authorization") token: String,
        @Body request: CheckoutRequest
    ): Response<CheckoutPostResponse>

    @GET("carts/checkout")
    suspend fun getCheckouts(
        @Header("Authorization") token: String
    ): Response<CheckoutGetResponse>


    @GET("products")
    suspend fun getProducts(
        @Header("Authorization") token: String
    ): Response<ProductResponse>

    @GET("products/{id}")
    suspend fun getProductDetail(
        @Path("id") productId: Int,
        @Header("Authorization") token: String
    ): ProductDetailResponse

    @POST("reviews")
    suspend fun postReview(
        @Header("Authorization") token: String,
        @Body review: ReviewRequest
    ): Response<ReviewResponse>

    @GET("categories/{id}")
    suspend fun getCategoryDetail(
        @Path("id") categoryId: Int,
        @Header("Authorization") token: String
    ): CategoryDetailResponse


    @GET("merchants/{id}")
    suspend fun getMerchantDetail(
        @Path("id") merchantId: Int,
        @Header("Authorization") token: String
    ): MerchantDetailResponse

    @Multipart
    @POST("predict")
    suspend fun predictImage(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part
    ): Response<PredictionResponse>

}