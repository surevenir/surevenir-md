package com.capstone.surevenir.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.surevenir.data.entity.MerchantDatabase
import com.capstone.surevenir.data.network.response.MerchantData
import com.capstone.surevenir.data.network.response.MerchantResponse
import com.capstone.surevenir.data.repository.MerchantRepository
import com.capstone.surevenir.model.Merchant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MerchantViewModel @Inject constructor(
    private val merchantRepository: MerchantRepository
) : ViewModel() {

    private val _merchantResponse = MutableLiveData<MerchantResponse?>()
    val merchantResponse: LiveData<MerchantResponse?> get() = _merchantResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun getMerchantsByMarketId(marketId: Int): Flow<List<MerchantDatabase>> {
        return merchantRepository.getMerchantsByMarketId(marketId)
    }

    fun getMerchants(token: String, onComplete: (List<MerchantData>?) -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                Log.d("MerchantViewModel", "Fetching merchants...")
                val response = merchantRepository.fetchMerchantsFromApi("Bearer $token") // Tambahkan "Bearer"
                _isLoading.value = false
                if (response.isSuccessful) {
                    val merchantList = response.body()?.data
                    _merchantResponse.value = response.body()

                    // Simpan ke database
                    merchantList?.let { merchants ->
                        Log.d("MerchantViewModel", "Saving ${merchants.size} merchants to database")
                        merchantRepository.updateLocalMerchants(
                            merchants.map { merchantData ->
                                MerchantDatabase(
                                    id = merchantData.id,
                                    name = merchantData.name,
                                    profileImageUrl = merchantData.profile_image_url,
                                    description = merchantData.description,
                                    longitude = merchantData.longitude,
                                    latitude = merchantData.latitude,
                                    userId = merchantData.user_id,
                                    marketId = merchantData.market_id,
                                    createdAt = merchantData.createdAt,
                                    updatedAt = merchantData.updatedAt,
                                    productsCount = merchantData.products_count,
                                    location = merchantData.location
                                )
                            }
                        )
                    }

                    onComplete(merchantList)
                } else {
                    _errorMessage.value = "Error: ${response.code()} - ${response.message()}"
                    Log.e("MerchantViewModel", "API Error: ${response.message()}")
                    onComplete(null)
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Failed: ${e.message}"
                Log.e("MerchantViewModel", "Exception: ${e.message}")
                e.printStackTrace() // Tambahkan stack trace untuk debugging
                onComplete(null)
            }
        }
    }
}


