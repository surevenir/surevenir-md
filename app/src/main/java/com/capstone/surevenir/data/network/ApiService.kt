package com.capstone.surevenir.data.network

import com.capstone.surevenir.model.Category
import com.capstone.surevenir.model.Merchant
import com.capstone.surevenir.model.Product
import com.capstone.surevenir.model.User
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface ApiService {

    @GET("users")
    fun getUsers(): Call<List<User>>



//    @GET("markets")
//    fun getMarkets(): Call<List<Market>>

    @GET("categories")
    fun getCategories(): Call<List<Category>>

    @GET("merchants")
    fun getMerchants(): Call<List<Merchant>>

    @GET("products")
    fun getProducts(): Call<List<Product>>

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