package com.capstone.surevenir.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "notifications")
data class NotificationDatabase(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val message: String,
    val locationId: String,
    val locationType: String,
    val numericId: Int,
    val timestamp: Date,
    val isRead: Boolean = false
)
