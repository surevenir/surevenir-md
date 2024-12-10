package com.capstone.surevenir.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.surevenir.helper.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TokenViewModel @Inject constructor(
    private val userPreferences: UserPreferences
): ViewModel(){

    private val _token = MutableLiveData<String?>()
    val token: LiveData<String?> = _token

    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String> = _userId

    fun fetchToken() {
        viewModelScope.launch {
            userPreferences.userToken.collect { tokenValue ->
                _token.value = tokenValue
                Log.d("TokenViewModel", "Token fetched: $tokenValue")

            }
        }
    }
}