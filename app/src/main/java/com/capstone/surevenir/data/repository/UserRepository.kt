package com.capstone.surevenir.data.repository

import com.capstone.surevenir.data.network.ApiService
import com.capstone.surevenir.data.network.response.AllUserResponse
import com.capstone.surevenir.data.network.response.CreateUserRequest
import com.capstone.surevenir.data.network.response.UpdateUserRequest
import com.capstone.surevenir.data.network.response.UserResponse
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getUsers(token: String): Response<AllUserResponse> {
        return apiService.getUsers("Bearer $token")
    }

    suspend fun getUserById(userId: String, token: String): Response<UserResponse> {
        return apiService.getUserById(userId, "Bearer $token")
    }

    fun createUser(request: CreateUserRequest) = apiService.createUser(request)

    suspend fun updateUser(
        userId: String,
        token: String,
        request: UpdateUserRequest,
        imageFile: File?
    ): Response<UserResponse> {
        val fullNameBody = request.fullName.toRequestBody("text/plain".toMediaTypeOrNull())
        val usernameBody = request.username.toRequestBody("text/plain".toMediaTypeOrNull())
        val phoneBody = request.phone?.toRequestBody("text/plain".toMediaTypeOrNull())
        val addressBody = request.address?.toRequestBody("text/plain".toMediaTypeOrNull())

        val imagePart = imageFile?.let { file ->
            val imageRequestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", file.name, imageRequestBody)
        }

        return apiService.updateUser(
            userId = userId,
            token = "Bearer $token",
            fullName = fullNameBody,
            username = usernameBody,
            phone = phoneBody,
            address = addressBody,
            image = imagePart
        )
    }
}
