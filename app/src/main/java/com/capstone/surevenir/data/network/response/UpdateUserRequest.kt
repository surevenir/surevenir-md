package com.capstone.surevenir.data.network.response

import com.google.gson.annotations.SerializedName

data class UpdateUserRequest(
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("address")
    val address: String? = null,
    @SerializedName("profile_image_url")
    val profileImageUrl: Any? = null
)