package com.capstone.surevenir.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.surevenir.data.network.response.CreateUserRequest
import com.capstone.surevenir.data.network.response.UpdateUserRequest
import com.capstone.surevenir.data.network.response.UserResponse
import com.capstone.surevenir.data.repository.UserRepository
import com.capstone.surevenir.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.Date
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

    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess: LiveData<Boolean> = _updateSuccess


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

    fun updateUser(userId: String, token: String, request: UpdateUserRequest, imageFile: File?) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = userRepository.updateUser(userId, token, request, imageFile)
                if (response.isSuccessful) {
                    _updateSuccess.value = true
                    _errorMessage.value = null
                } else {
                    _updateSuccess.value = false
                    _errorMessage.value = "Update failed: ${response.message()}"
                }
            } catch (e: Exception) {
                _updateSuccess.value = false
                _errorMessage.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
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

    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> get() = _currentUser

    fun getUserById(userEmail: String, token: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val usersResponse = userRepository.getUsers(token)
                if (usersResponse.isSuccessful) {
                    val userList = usersResponse.body()?.data
                    val user = userList?.find { it.email == userEmail }

                    if (user != null) {
                        val detailResponse = userRepository.getUserById(user.id, token)
                        _isLoading.value = false

                        if (detailResponse.isSuccessful) {
                            val userData = detailResponse.body()?.data
                            Log.d("UserData", "Received user data: $userData")
                            val fullUser = userData?.let {
                                User(
                                    id = it.id ?: user.id,
                                    fullName = it.fullName ?: "Default Name",
                                    username = it.username ?: user.username,
                                    email = it.email ?: user.email,
                                    profileImageUrl = it.profileImageUrl?.toString() ?: user.profileImageUrl,
                                    phone = it.phone,
                                    address = it.address,
                                    createdAt = Date(),
                                    updatedAt = Date()
                                )
                            }
                            _currentUser.value = fullUser
                        } else {
                            _errorMessage.value = "Error: ${detailResponse.code()} - ${detailResponse.message()}"
                        }
                    } else {
                        _errorMessage.value = "User not found"
                    }
                } else {
                    _errorMessage.value = "Error: ${usersResponse.code()} - ${usersResponse.message()}"
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Failed: ${e.message}"
            }
        }
    }


    fun clearState() {
        _userResponse.value = null
        _users.value = null
        _errorMessage.value = null
    }

}
