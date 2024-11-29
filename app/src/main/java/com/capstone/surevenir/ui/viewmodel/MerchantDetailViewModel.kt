package com.capstone.surevenir.ui.viewmodel

import androidx.compose.ui.util.unpackFloat1
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.surevenir.data.network.response.MerchantDetail
import com.capstone.surevenir.data.network.response.ProductDetail
import com.capstone.surevenir.data.repository.MerchantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MerchantDetailViewModel @Inject constructor(
    private val repository: MerchantRepository
): ViewModel() {

    private val _merchantDetail = MutableStateFlow<MerchantDetail?>(null)
    val merchantDetail: StateFlow<MerchantDetail?> = _merchantDetail

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchMerchantDetail(merchantId: Int, token: String){
        viewModelScope.launch {
            try {
                val response = repository.getMerchantDetail(merchantId, token)
                if (response.success) {
                    _merchantDetail.value = response.data
                } else {
                    _error.value = response.message
                }
            }
            catch (e: Exception){
                _error.value = e.message
            }
        }
    }

}