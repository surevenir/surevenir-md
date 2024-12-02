package com.capstone.surevenir.ui.viewmodel

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.surevenir.data.network.RetrofitInstance
import com.capstone.surevenir.data.network.response.GeocodingResponse
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class GeocodingViewModel : ViewModel() {

    companion object {
        private const val REQUEST_CODE_LOCATION_PERMISSION = 123
    }

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
    fun getCurrentLocationAndGetSubDistrict(
        context: Context,
        apiKey: String,
        onResult: (String?) -> Unit
    ) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    getSubDistrictFromCoordinates(location.latitude, location.longitude, apiKey, onResult)
                } else {
                    onResult(null)
                }
            }.addOnFailureListener { e ->
                Log.e("LocationException", "Exception: ${e.message}")
                onResult(null)
            }
        } else {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION_PERMISSION
            )
            onResult(null)
        }
    }
}

