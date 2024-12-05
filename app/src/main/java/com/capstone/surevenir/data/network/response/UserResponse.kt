package com.capstone.surevenir.data.network.response

import com.capstone.surevenir.model.User
import com.google.gson.annotations.SerializedName

data class UserResponse(
	val statusCode: Int? = null,
	val data: Data? = null,
	val success: Boolean? = null,
	val message: String? = null
)

data class AllUserResponse(
	val statusCode: Int? = null,
	val data: List<User>,
	val success: Boolean? = null,
	val message: String? = null
)

data class Data(
	val role: String? = null,
	val address: String? = null,
	val latitude: String? = null,
	val profileImageUrl: Any? = null,
	val createdAt: String? = null,
	val password: String? = null,
	val fullName: String? = null,
	val phone: String? = null,
	val provider: String? = null,
	val id: String? = null,
	val email: String? = null,
	val username: String? = null,
	val longitude: String? = null,
	val updatedAt: String? = null
)

data class CreateUserRequest(
	val id: String,
	@SerializedName("full_name")
	val fullName: String,
	val username: String,
	val email: String,
	val password: String? = null,
	val phone: String? = null,
	val role: String = "USER",
	val provider: String = "EMAIL",
	val longitude: String? = null,
	val latitude: String? = null,
	val address: String? = null
)

