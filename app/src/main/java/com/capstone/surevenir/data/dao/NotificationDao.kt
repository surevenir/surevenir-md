package com.capstone.surevenir.data.dao


import androidx.room.*
import com.capstone.surevenir.data.entity.NotificationDatabase
import kotlinx.coroutines.flow.Flow

    @Dao
    interface NotificationDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insertNotification(notification: NotificationDatabase)

        @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
        fun getAllNotifications(): Flow<List<NotificationDatabase>>

        @Query("SELECT * FROM notifications WHERE isRead = 0 ORDER BY timestamp DESC")
        fun getUnreadNotifications(): Flow<List<NotificationDatabase>>

        @Query("SELECT COUNT(*) FROM notifications WHERE isRead = 0")
        fun getUnreadCount(): Flow<Int>

        @Query("UPDATE notifications SET isRead = 1 WHERE id = :notificationId")
        fun markAsRead(notificationId: Long)

        @Query("UPDATE notifications SET isRead = 1")
        fun markAllAsRead()

        @Query("DELETE FROM notifications WHERE id = :notificationId")
       fun deleteNotification(notificationId: Long)

        @Query("DELETE FROM notifications")
        fun deleteAllNotifications()

        @Query("SELECT * FROM notifications WHERE locationType = :type ORDER BY timestamp DESC")
        fun getNotificationsByType(type: String): Flow<List<NotificationDatabase>>
    }