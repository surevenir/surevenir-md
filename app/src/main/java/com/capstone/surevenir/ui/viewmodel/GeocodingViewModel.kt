package com.capstone.surevenir.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.surevenir.data.network.RetrofitInstance
import com.capstone.surevenir.data.network.response.GeocodingResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class GeocodingViewModel : ViewModel() {
    fun getSubDistrictFromCoordinates(
        lat: Double,
        lng: Double,
        apiKey: String,
        onResult: (String?) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val latlng = "$lat,$lng"
                val response = RetrofitInstance.api.getAddress(latlng, apiKey).execute()
                if (response.isSuccessful) {
                    val results = response.body()?.results ?: emptyList()
                    val subDistrict = results.firstOrNull()?.address_components?.firstOrNull {
                        it.types.contains("sublocality") ||
                                it.types.contains("administrative_area_level_2") ||
                                it.types.contains("locality")
                    }?.long_name
                    onResult(subDistrict)
                } else {
                    Log.e("GeocodingError", "API Error: ${response.errorBody()?.string()}")
                    onResult(null)
                }
            } catch (e: Exception) {
                Log.e("GeocodingException", "Exception: ${e.message}")
                onResult(null)
            }
        }
    }
}

