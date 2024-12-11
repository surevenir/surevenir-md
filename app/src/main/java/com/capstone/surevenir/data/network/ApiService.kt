package com.capstone.surevenir.data.network

import android.service.autofill.UserData
import com.capstone.surevenir.data.network.response.AllUserResponse
import com.capstone.surevenir.data.network.response.CartResponse
import com.capstone.surevenir.data.network.response.CategoryDetailResponse
import com.capstone.surevenir.data.network.response.CategoryResponse
import com.capstone.surevenir.data.network.response.CheckoutGetResponse
import com.capstone.surevenir.data.network.response.CheckoutPostResponse
import com.capstone.surevenir.data.network.response.CheckoutRequest
import com.capstone.surevenir.data.network.response.CheckoutResponse
import com.capstone.surevenir.data.network.response.CreateUserRequest
import com.capstone.surevenir.data.network.response.DeleteCartResponse
import com.capstone.surevenir.data.network.response.DeleteFavoriteResponse
import com.capstone.surevenir.data.network.response.FavoriteResponse
import com.capstone.surevenir.data.network.response.GetFavoriteProductsResponse
import com.capstone.surevenir.data.network.response.LeaderboardResponse
import com.capstone.surevenir.data.network.response.MarketDetailResponse
import com.capstone.surevenir.data.network.response.MarketResponse
import com.capstone.surevenir.data.network.response.MerchantDetailResponse
import com.capstone.surevenir.data.network.response.MerchantResponse
import com.capstone.surevenir.data.network.response.PredictionResponse
import com.capstone.surevenir.data.network.response.ProductDetailResponse
import com.capstone.surevenir.data.network.response.ProductResponse
import com.capstone.surevenir.data.network.response.ReviewRequest
import com.capstone.surevenir.data.network.response.ReviewResponse
import com.capstone.surevenir.data.network.response.ReviewsResponse
import com.capstone.surevenir.data.network.response.ScanHistoryResponse
import com.capstone.surevenir.data.network.response.UpdateUserRequest
import com.capstone.surevenir.data.network.response.UserResponse
import com.capstone.surevenir.model.Category
import com.capstone.surevenir.model.CreateCartRequest
import com.capstone.surevenir.model.CreateCartResponse
import com.capstone.surevenir.model.Market
import com.capstone.surevenir.model.Merchant
import com.capstone.surevenir.model.Product
import com.capstone.surevenir.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
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
import retrofit2.http.Query


interface ApiService {

    @GET("users")
    suspend fun getUsers(@Header("Authorization") token: String): Response<AllUserResponse>
    @POST("users")
    fun createUser(@Body request: CreateUserRequest): Call<UserResponse>


    @POST("carts")
    suspend fun createCarts(
        @Header("Authorization") token: String,
        @Body request: CreateCartRequest
    ): Response<CreateCartResponse>


    @GET("categories")
    suspend fun getCategories(@Header("Authorization") token: String): Response<CategoryResponse>

    @GET("merchants")
    suspend fun getMerchants(@Header("Authorization") token: String): Response<MerchantResponse>

    @GET("markets")
    suspend fun getMarkets(@Header("Authorization") token: String): Response<MarketResponse>

        @GET("markets/{id}")
        suspend fun getMarketDetail(
            @Path("id") marketId: Int,
            @Header("Authorization") token: String
        ): MarketDetailResponse

    @GET("predict/histories/me")
    suspend fun getUserScanHistory(
        @Header("Authorization") token: String
    ): ScanHistoryResponse



    @GET("products/{id}/reviews")
    suspend fun getReviews(
        @Path("id") productId: Int,
        @Header("Authorization") token: String
    ): Response<ReviewsResponse>

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

    @GET("carts")
    suspend fun getCart(
        @Header("Authorization") token: String
    ): Response<CartResponse>

    @GET("products/favorites")
    suspend fun getFavoriteProducts(
        @Header("Authorization") token: String
    ): GetFavoriteProductsResponse

    @PATCH("carts/{id}")
    suspend fun updateCartQuantity(
        @Path("id") cartItemId: Int,
        @Header("Authorization") token: String,
        @Body quantity: Map<String, Int>
    ): Response<CartResponse>


    @DELETE("carts/{id}")
    suspend fun deleteCartItem(
        @Path("id") cartItemId: Int,
        @Header("Authorization") token: String
    ): Response<DeleteCartResponse>

    @POST("carts/checkout")
    suspend fun checkout(
        @Header("Authorization") token: String,
        @Body request: CheckoutRequest
    ): Response<CheckoutPostResponse>

    @GET("carts/checkout")
    suspend fun getCheckouts(
        @Header("Authorization") token: String
    ): Response<CheckoutGetResponse>

    @DELETE("products/{productId}/favorites")
    suspend fun deleteFavorite(
        @Path("productId") productId: Int,
        @Header("Authorization") token: String
    ): DeleteFavoriteResponse

    @GET("products")
    suspend fun getProducts(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<ProductResponse>

    @GET("products/{id}")
    suspend fun getProductDetail(
        @Path("id") productId: Int,
        @Header("Authorization") token: String
    ): ProductDetailResponse

    @GET("categories/{id}")
    suspend fun getCategoryDetail(
        @Path("id") categoryId: Int,
        @Header("Authorization") token: String
    ): CategoryDetailResponse

    @POST("products/{id}/favorites")
    suspend fun addToFavorites(
        @Path("id") productId: Int,
        @Header("Authorization") token: String
    ): FavoriteResponse


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

    @Multipart
    @POST("predict")
    suspend fun predictImage(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part
    ): Response<PredictionResponse>

    @Multipart
    @POST("reviews")
    suspend fun postReview(
        @Header("Authorization") token: String,
        @Part("rating") rating: RequestBody,
        @Part("comment") comment: RequestBody,
        @Part("product_id") productId: RequestBody,
        @Part images: List<MultipartBody.Part>
    ): Response<ReviewResponse>

    @GET("predict/top-scanner")
    suspend fun getLeaderboard(@Header("Authorization") token: String): LeaderboardResponse

}