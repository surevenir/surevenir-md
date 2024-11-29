package com.capstone.surevenir.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.surevenir.data.network.response.CreateUserRequest
import com.capstone.surevenir.data.network.response.UserResponse
import com.capstone.surevenir.data.repository.UserRepository
import com.capstone.surevenir.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userResponse = MutableLiveData<UserResponse?>()
    val userResponse: LiveData<UserResponse?> get() = _userResponse

    private val _users = MutableLiveData<List<User>?>()
    val users: LiveData<List<User>?> get() = _users

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun createUser(request: CreateUserRequest) {
        _isLoading.value = true
        userRepository.createUser(request).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userResponse.value = response.body()
                } else {
                    _errorMessage.value = "Error: ${response.code()} - ${response.message()}"
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Failed: ${t.message}"
            }
        })
    }

    fun getUsers(token: String, onComplete: (List<User>?) -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = userRepository.getUsers(token)
                _isLoading.value = false
                if (response.isSuccessful) {
                    val userList = response.body()?.data
                    _users.value = userList
                    Log.d("DEBUG", "API Response Successful: $userList")
                    onComplete(userList)
                } else {
                    _errorMessage.value = "Error: ${response.code()} - ${response.message()}"
                    Log.d("DEBUG", "API Response Error: ${response.code()} - ${response.message()}")
                    onComplete(null)
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Failed: ${e.message}"
                Log.d("DEBUG", "Exception in API Call: ${e.message}")
                onComplete(null)
            }
        }
    }




    fun clearState() {
        _userResponse.value = null
        _users.value = null
        _errorMessage.value = null
    }

}
