package com.capstone.surevenir.receivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.capstone.surevenir.MainActivity
import com.capstone.surevenir.R
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    companion object {
        private const val CHANNEL_ID = "geofence_channel"
        private const val CHANNEL_NAME = "Geofence Notifications"
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("GeofenceReceiver", "Broadcast received with intent action: ${intent.action}")
        Log.d("GeofenceReceiver", "Intent extras: ${intent.extras?.keySet()?.joinToString()}")

        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent == null) {
            Log.e("GeofenceReceiver", "Null GeofencingEvent")
            return
        }

        if (geofencingEvent.hasError()) {
            val errorMessage = getErrorString(geofencingEvent.errorCode)
            Log.e("GeofenceReceiver", "Error code: ${geofencingEvent.errorCode}, Message: $errorMessage")
            return
        }

        val geofenceTransition = geofencingEvent.geofenceTransition
        Log.d("GeofenceReceiver", "Geofence Transition: $geofenceTransition")

        when (geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                val triggeringGeofences = geofencingEvent.triggeringGeofences
                Log.d("GeofenceReceiver", "Triggering geofences: ${triggeringGeofences?.size}")

                triggeringGeofences?.forEach { geofence ->
                    val locationId = geofence.requestId
                    Log.d("GeofenceReceiver", "Triggered geofence ID: $locationId")

                    val title = when {
                        locationId.startsWith("merchant_") -> "Shop Nearby!"
                        locationId.startsWith("market_") -> "Market Nearby!"
                        else -> "Location Alert!"
                    }

                    val message = when {
                        locationId.startsWith("merchant_") -> "There's a recommended shop nearby. Click to explore!"
                        locationId.startsWith("market_") -> "You're near a traditional market. Click to see more!"
                        else -> "You've entered a marked location"
                    }

                    showNotification(context, locationId, title, message)
                }
            }
            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                Log.d("GeofenceReceiver", "Exited geofence area")
            }
            else -> {
                Log.e("GeofenceReceiver", "Invalid transition type: $geofenceTransition")
            }
        }
    }

    private fun showNotification(context: Context, geofenceId: String, title: String, message: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val numericId = geofenceId.substringAfter("_").toIntOrNull() ?: return
        val destination = when {
            geofenceId.startsWith("merchant_") -> "merchant/$numericId"
            geofenceId.startsWith("market_") -> "market/$numericId"
            else -> null
        }

        Log.d("GeofenceReceiver", "Setting destination: $destination")




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = "Notifications for nearby locations"
                enableLights(true)
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("navigate_to", destination)
            putExtra("from_notification", true)
            putExtra("id", numericId)  // Save numeric ID
        }



        val pendingIntent = PendingIntent.getActivity(
            context,
            numericId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(pendingIntent)
            .build()

        val notificationId = System.currentTimeMillis().toInt()
        notificationManager.notify(notificationId, notification)

        Log.d("GeofenceReceiver", "Notification shown: ID=$notificationId, Title=$title")
    }

    private fun getErrorString(errorCode: Int): String {
        return when (errorCode) {
            1 -> "GEOFENCE_NOT_AVAILABLE"
            2 -> "GEOFENCE_TOO_MANY_GEOFENCES"
            3 -> "GEOFENCE_TOO_MANY_PENDING_INTENTS"
            -1 -> "GEOFENCE_INSUFFICIENT_LOCATION_PERMISSION"
            else -> "Unknown error code: $errorCode"
        }
    }
}