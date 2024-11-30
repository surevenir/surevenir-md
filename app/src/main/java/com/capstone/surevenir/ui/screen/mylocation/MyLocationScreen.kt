package com.capstone.surevenir.ui.screen.mylocation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.MarkerState

@Composable
fun MyLocationScreen(navController: NavController) {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val locationPermissionGranted = remember { mutableStateOf(false) }
    val location = remember { mutableStateOf<LatLng?>(null) }

    // Menangani izin lokasi
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            locationPermissionGranted.value = isGranted
        }

    LaunchedEffect(Unit) {
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                locationPermissionGranted.value = true
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    // Menangani lokasi setelah izin diberikan
    LaunchedEffect(locationPermissionGranted.value) {
        if (locationPermissionGranted.value) {
            fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                loc?.let {
                    location.value = LatLng(it.latitude, it.longitude)
                }
            }
        }
    }

    // Menampilkan peta jika lokasi sudah ada
    location.value?.let { userLocation ->
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(userLocation, 14f)
        }

        // Using GoogleMap composable from maps-compose
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            // Corrected usage of Marker with state parameter
            Marker(
                state = MarkerState(position = userLocation), // Use `MarkerState` here
                title = "Lokasi Saya",
                snippet = "Ini adalah lokasi saya yang sebenarnya"
            )
        }
    }

    // UI jika izin lokasi belum diberikan atau lokasi belum didapatkan
    if (!locationPermissionGranted.value) {
        Text("Meminta izin lokasi...")
    } else if (location.value == null) {
        Text("Mendapatkan lokasi...")
    }

    // Button untuk pindah ke halaman berikutnya
    Button(onClick = { navController.navigate("nextDestination") }) {
        Text("Ke Halaman Berikutnya")
    }
}
