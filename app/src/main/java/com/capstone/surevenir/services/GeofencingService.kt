package com.capstone.surevenir.services


import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.capstone.surevenir.R
import com.capstone.surevenir.receivers.GeofenceBroadcastReceiver
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

class GeofencingService : Service() {
    private lateinit var geofencingClient: GeofencingClient

    override fun onCreate() {
        super.onCreate()
        geofencingClient = LocationServices.getGeofencingClient(this)
        Log.d("GeofencingService", "Service Created")  // Tambahkan log ini

        // Wajib untuk Android O+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "geofence_service",
                "Geofence Service",
                NotificationManager.IMPORTANCE_HIGH  // Ubah ke HIGH
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)

            val notification = NotificationCompat.Builder(this, "geofence_service")
                .setContentTitle("Location Service Active")
                .setContentText("Monitoring nearby locations")
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setPriority(NotificationCompat.PRIORITY_HIGH)  // Tambahkan ini
                .build()

            Log.d("GeofencingService", "Starting foreground service")  // Tambahkan log ini
            startForeground(1, notification)
        }
    }

    private fun addGeofence(id: String, latitude: Double, longitude: Double, radius: Float = 1000f) {
        // Debug log
        Log.d("GeofencingService", "Starting addGeofence for $id at $latitude, $longitude")

        // Check permissions
        if (!hasRequiredPermissions()) {
            Log.e("GeofencingService", "Missing required permissions")
            return
        }

        try {
            val geofence = Geofence.Builder()
                .setRequestId(id)
                .setCircularRegion(latitude, longitude, radius)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .setNotificationResponsiveness(1000) // 1 second
                .build()

            val geofencingRequest = GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build()

            val pendingIntent = getGeofencePendingIntent()

            // Remove existing geofences first
            geofencingClient.removeGeofences(pendingIntent)
                .addOnCompleteListener {
                    // Add new geofence
                    geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                        .addOnSuccessListener {
                            Log.d("GeofencingService", "Successfully added geofence: $id")
                        }
                        .addOnFailureListener { e ->
                            Log.e("GeofencingService", "Failed to add geofence: $id", e)
                            e.printStackTrace()
                        }
                }
        } catch (e: SecurityException) {
            Log.e("GeofencingService", "Security exception: ${e.message}")
            e.printStackTrace()
        } catch (e: Exception) {
            Log.e("GeofencingService", "Exception: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun hasRequiredPermissions(): Boolean {
        val fineLocation = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val backgroundLocation = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }

        val notifications = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }

        Log.d("GeofencingService", "Permissions check: Fine=$fineLocation, Background=$backgroundLocation, Notifications=$notifications")

        return fineLocation && backgroundLocation && notifications
    }

    private fun getGeofencePendingIntent(): PendingIntent {
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        return PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("GeofencingService", "onStartCommand called")

        val id = intent?.getStringExtra("id")
        val lat = intent?.getDoubleExtra("lat", Double.NaN)
        val lng = intent?.getDoubleExtra("lng", Double.NaN)

        if (id == null || lat == null || lng == null || lat.isNaN() || lng.isNaN()) {
            Log.e("GeofencingService", "Invalid parameters: id=$id, lat=$lat, lng=$lng")
            return START_NOT_STICKY
        }

        Log.d("GeofencingService", "Adding geofence: id=$id, lat=$lat, lng=$lng")
        addGeofence(id, lat, lng)

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
