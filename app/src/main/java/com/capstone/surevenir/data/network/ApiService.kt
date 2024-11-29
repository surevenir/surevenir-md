package com.capstone.surevenir.data.network

import android.service.autofill.UserData
import com.capstone.surevenir.data.network.response.AllUserResponse
import com.capstone.surevenir.data.network.response.CategoryResponse
import com.capstone.surevenir.data.network.response.CreateUserRequest
import com.capstone.surevenir.data.network.response.MarketResponse
import com.capstone.surevenir.data.network.response.MerchantDetailResponse
import com.capstone.surevenir.data.network.response.MerchantResponse
import com.capstone.surevenir.data.network.response.ProductDetailResponse
import com.capstone.surevenir.data.network.response.ProductResponse
import com.capstone.surevenir.data.network.response.UserResponse
import com.capstone.surevenir.model.Category
import com.capstone.surevenir.model.Market
import com.capstone.surevenir.model.Merchant
import com.capstone.surevenir.model.Product
import com.capstone.surevenir.model.User
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
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

    @GET("products")
    suspend fun getProducts(
        @Header("Authorization") token: String
    ): Response<ProductResponse>

    @GET("products/{id}")
    suspend fun getProductDetail(
        @Path("id") productId: Int,
        @Header("Authorization") token: String
    ): ProductDetailResponse


    @GET("merchants/{id}")
    suspend fun getMerchantDetail(
        @Path("id") merchantId: Int,
        @Header("Authorization") token: String
    ): MerchantDetailResponse

    @GET("reviews")
//    fun getReviews(): Call<List<Review>>

//    @GET("carts")
//    fun getCarts(): Call<List<Cart>>

    @Multipart
    @POST("upload")
    fun uploadImages(
        @Part images: List<MultipartBody.Part>
    ): Call<ResponseBody>

//    @GET("count")
//    fun getStatistics(): Call<StatisticsResponse>

}