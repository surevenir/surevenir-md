package com.capstone.surevenir.helper

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.room.Room
import com.capstone.surevenir.MainActivity
import com.capstone.surevenir.R
import com.capstone.surevenir.data.database.AppDatabase
import com.capstone.surevenir.data.entity.NotificationDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class FirebaseMessaging : FirebaseMessagingService() {
    private val TAG = "FirebaseMessaging"

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "New token: $token")
    }

    @SuppressLint("MissingPermission")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val title = remoteMessage.notification?.title ?: "No Title"
        val body = remoteMessage.notification?.body ?: "No Body"

        val type = remoteMessage.data["type"] ?: "general"
        val locationId = remoteMessage.data["locationId"]
        val numericId = remoteMessage.data["numericId"]?.toIntOrNull()

        val navigationRoute = when (type) {
            "market" -> "market/$numericId"
            "merchant" -> "merchant/$numericId"
            else -> null
        }
        createNotificationChannel()
        showNotification(title, body, navigationRoute)


        saveNotificationToDatabase(
            title = title,
            message = body,
            type = type,
            locationId = locationId,
            numericId = numericId
        )
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(title: String, body: String, navigationRoute: String?) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("navigate_to", navigationRoute)
        }

        val requestCode = System.currentTimeMillis().toInt()

        val pendingIntent = PendingIntent.getActivity(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.surevenir_logo_home)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)

        with(NotificationManagerCompat.from(this)) {
            notify(requestCode, builder.build())
        }

        Log.d(TAG, "Showing notification with route: $navigationRoute")
    }


    private fun saveNotificationToDatabase(
        title: String,
        message: String,
        type: String,
        locationId: String? = null,
        numericId: Int? = null
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val database = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java,
                    "surevenir-database"
                ).build()

                val notificationDao = database.notificationDao()

                val notification = NotificationDatabase(
                    title = title,
                    message = message,
                    locationId = locationId ?: "",
                    locationType = type,
                    numericId = numericId ?: -1,
                    timestamp = Date(),
                    isRead = false
                )

                notificationDao.insertNotification(notification)
                Log.d(TAG, "Notification saved to database successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Error saving notification to database", e)
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Default Channel"
            val descriptionText = "This is the default channel for app notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableLights(true)
                enableVibration(true)
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "default_channel_id"
    }
}