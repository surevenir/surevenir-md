package com.capstone.surevenir.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.surevenir.data.network.response.ScanHistory
import com.capstone.surevenir.data.repository.ScanHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanHistoryViewModel @Inject constructor(
    private val repository: ScanHistoryRepository
) : ViewModel() {
    private val _scanHistory = MutableStateFlow<List<ScanHistory>>(emptyList())
    val scanHistory = _scanHistory.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun fetchScanHistory(token: String?) {
        if (token == null) {
            _error.value = "No token available"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.getUserScanHistory(token)
                .onSuccess { histories ->
                    _scanHistory.value = histories
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }

            _isLoading.value = false
        }
    }
}