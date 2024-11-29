package com.capstone.surevenir.data.repository

import com.capstone.surevenir.data.network.ApiService
import com.capstone.surevenir.data.network.response.AllUserResponse
import com.capstone.surevenir.data.network.response.CreateUserRequest
import com.capstone.surevenir.data.network.response.UserResponse
import com.capstone.surevenir.model.User
import retrofit2.Response
import javax.inject.Inject

//Tes

class UserRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getUsers(token: String): Response<AllUserResponse> {
        return apiService.getUsers("Bearer $token")
    }

    fun createUser(request: CreateUserRequest) = apiService.createUser(request)
}
