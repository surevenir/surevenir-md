package com.capstone.surevenir.data.network.response

data class LeaderboardResponse(
    val success: Boolean,
    val status_code: Int,
    val message: String,
    val data: List<LeaderboardUser>
)

data class LeaderboardUser(
    val user_id: String,
    val user_name: String,
    val email: String,
    val profile_image_url: String,
    val history_count: Int,
    val points: Int
)
