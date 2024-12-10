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
    val locationId: String,  // merchant_id or market_id
    val locationType: String, // "merchant" or "market"
    val numericId: Int,      // The actual ID of merchant or market
    val timestamp: Date,
    val isRead: Boolean = false
)
