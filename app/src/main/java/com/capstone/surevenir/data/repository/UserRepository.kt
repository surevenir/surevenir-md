package com.capstone.surevenir.data.repository

import com.capstone.surevenir.data.network.ApiService
import com.capstone.surevenir.data.network.response.CreateUserRequest
import com.capstone.surevenir.model.User
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getUsers(): Response<List<User>> {
        return apiService.getUsers()
    }
    fun createUser(request: CreateUserRequest) = apiService.createUser(request)
}
