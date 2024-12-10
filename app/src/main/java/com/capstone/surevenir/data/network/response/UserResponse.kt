package com.capstone.surevenir.data.network.response

import com.capstone.surevenir.model.User
import com.google.gson.annotations.SerializedName

data class UserResponse(
	@SerializedName("status_code")
	val statusCode: Int? = null,
	@SerializedName("data")
	val data: Data? = null,
	@SerializedName("success")
	val success: Boolean? = null,
	@SerializedName("message")
	val message: String? = null
)

data class AllUserResponse(
	@SerializedName("status_code")
	val statusCode: Int? = null,
	@SerializedName("data")
	val data: List<User>,
	@SerializedName("success")
	val success: Boolean? = null,
	@SerializedName("message")
	val message: String? = null
)

data class Data(
	@SerializedName("role")
	val role: String? = null,
	@SerializedName("address")
	val address: String? = null,
	@SerializedName("latitude")
	val latitude: String? = null,
	@SerializedName("profile_image_url")
	val profileImageUrl: Any? = null,
	@SerializedName("createdAt")
	val createdAt: String? = null,
	@SerializedName("password")
	val password: String? = null,
	@SerializedName("full_name")
	val fullName: String? = null,
	@SerializedName("phone")
	val phone: String? = null,
	@SerializedName("provider")
	val provider: String? = null,
	@SerializedName("id")
	val id: String? = null,
	@SerializedName("email")
	val email: String? = null,
	@SerializedName("username")
	val username: String? = null,
	@SerializedName("longitude")
	val longitude: String? = null,
	@SerializedName("updatedAt")
	val updatedAt: String? = null
)

data class CreateUserRequest(
	@SerializedName("id")
	val id: String,
	@SerializedName("full_name")
	val fullName: String,
	@SerializedName("username")
	val username: String,
	@SerializedName("email")
	val email: String,
	@SerializedName("password")
	val password: String? = null,
	@SerializedName("phone")
	val phone: String? = null,
	@SerializedName("role")
	val role: String = "USER",
	@SerializedName("provider")
	val provider: String = "EMAIL",
	@SerializedName("longitude")
	val longitude: String? = null,
	@SerializedName("latitude")
	val latitude: String? = null,
	@SerializedName("address")
	val address: String? = null
)