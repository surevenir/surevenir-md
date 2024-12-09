package com.capstone.surevenir.helper

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.capstone.surevenir.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessaging : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Di sini Anda dapat menyimpan token ke server jika perlu
        // misalnya ServerAPI.saveToken(token)
    }

    @SuppressLint("MissingPermission")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Ambil data notifikasi dari remoteMessage
        val title = remoteMessage.notification?.title ?: "No Title"
        val body = remoteMessage.notification?.body ?: "No Body"

        // Jika menggunakan data payload:
        // val dataPayload = remoteMessage.data // ini berupa Map<String, String>

        // Buat notification channel (untuk Android O ke atas)
        createNotificationChannel()

        // Bangun notifikasi menggunakan NotificationCompat
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_notifications_24) // ganti dengan icon notifikasi Anda
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        // Tampilkan notifikasi
        with(NotificationManagerCompat.from(this)) {
            notify(System.currentTimeMillis().toInt(), builder.build())
        }

    }

    private fun createNotificationChannel() {
        // Hanya diperlukan jika targetSdkVersion >= 26 (Android O)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Default Channel"
            val descriptionText = "This is the default channel for app notifications."
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            // Daftarkan channel ke sistem
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "default_channel_id"
    }
}
