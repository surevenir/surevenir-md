package com.capstone.surevenir.data.network.response

import com.google.gson.annotations.SerializedName

data class UpdateUserRequest(
    @SerializedName("full_name") val fullName: String? = null,
    val username: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val profileImageUrl: Any? = null,
    val latitude: String? = null,
    val longitude: String? = null
)