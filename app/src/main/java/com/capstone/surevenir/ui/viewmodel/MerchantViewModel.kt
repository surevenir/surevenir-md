package com.capstone.surevenir.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.surevenir.data.network.response.MerchantData
import com.capstone.surevenir.data.network.response.MerchantResponse
import com.capstone.surevenir.data.repository.MerchantRepository
import com.capstone.surevenir.model.Merchant
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun getMerchants(token: String, onComplete: (List<Merchant>?) -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = merchantRepository.getMerchants(token)
                _isLoading.value = false
                if (response.isSuccessful) {
                    val merchantDataList = response.body()?.data
                    val merchantList = merchantDataList?.toMerchants()
                    _merchantResponse.value = response.body()
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
                onComplete(null)
            }
        }
    }

}

private fun List<MerchantData>.toMerchants(): List<Merchant> {
    return this.map { merchantData ->
        Merchant(
            id = merchantData.id,
            name = merchantData.name,
            profile_image_url = merchantData.profile_image_url ?: "",
            description = merchantData.description ?: "",
            longitude = merchantData.longitude ?: "",
            latitude = merchantData.latitude ?: "",
            user_id = merchantData.user_id ?: "",
            market_id = merchantData.market_id ?: 0,
            createdAt = merchantData.createdAt ?: "",
            updatedAt = merchantData.updatedAt ?: "",
            shopLocation = "${merchantData.latitude}, ${merchantData.longitude}",
            products_count = merchantData.products_count,
        )
    }
}

