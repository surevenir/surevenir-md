package com.capstone.surevenir.data.network

import com.capstone.surevenir.data.network.response.GeocodingResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApiService {
    @GET("geocode/json")
    fun getAddress(
        @Query("latlng") latlng: String,
        @Query("key") apiKey: String
    ): Call<GeocodingResponse>
}