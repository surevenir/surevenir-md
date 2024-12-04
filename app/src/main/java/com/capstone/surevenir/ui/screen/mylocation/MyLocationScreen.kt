package com.capstone.surevenir.ui.screen.mylocation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.capstone.surevenir.data.network.response.MerchantData
import com.capstone.surevenir.model.Market
import com.capstone.surevenir.ui.screen.navmenu.sfui_semibold
import com.capstone.surevenir.ui.viewmodel.MarketViewModel
import com.capstone.surevenir.ui.viewmodel.MerchantViewModel
import com.capstone.surevenir.ui.viewmodel.TokenViewModel
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerState

@Composable
fun MyLocationScreen(
    navController: NavController,
    merchantViewModel: MerchantViewModel = hiltViewModel(),
    marketViewModel: MarketViewModel = hiltViewModel(),
    tokenViewModel: TokenViewModel = hiltViewModel()
) {
    val token by tokenViewModel.token.observeAsState()
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val scope = rememberCoroutineScope()

    var locationPermissionGranted by remember { mutableStateOf(false) }
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    var merchants by remember { mutableStateOf<List<MerchantData>?>(null) }
    var markets by remember { mutableStateOf<List<Market>?>(null) }

    fun loadData(currentToken: String?) {
        if (currentToken == null) {
            Log.e("MyLocationScreen", "Token is null, cannot load data")
            return
        }

        val bearerToken = "Bearer $currentToken"
        Log.d("MyLocationScreen", "Loading data with token: $bearerToken")

        merchantViewModel.getMerchants(bearerToken) { merchantList ->
            merchants = merchantList
            Log.d("MyLocationScreen", "Merchants loaded: ${merchantList?.size}")
        }

        marketViewModel.getMarkets(bearerToken) { marketList ->
            markets = marketList
            Log.d("MyLocationScreen", "Markets loaded: ${marketList?.size}")
        }
    }

    LaunchedEffect(token) {
        Log.d("MyLocationScreen", "Token changed: $token")
        if (token != null) {
            loadData(token)
        }
    }

    LaunchedEffect(Unit) {
        Log.d("MyLocationScreen", "Fetching initial token")
        tokenViewModel.fetchToken()
    }

    fun checkGpsEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun openGpsSettings() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        context.startActivity(intent)
    }

    fun updateLocation() {
        if (!checkGpsEnabled()) {
            Toast.makeText(context, "Mohon aktifkan GPS", Toast.LENGTH_SHORT).show()
            openGpsSettings()
            return
        }

        try {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val currentLocationRequest = CurrentLocationRequest.Builder()
                    .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                    .setDurationMillis(5000)
                    .setMaxUpdateAgeMillis(0)
                    .build()

                fusedLocationClient.getCurrentLocation(currentLocationRequest, null)
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            currentLocation = LatLng(location.latitude, location.longitude)
                            isLoading = false
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Gagal mendapatkan lokasi", Toast.LENGTH_SHORT).show()
                        isLoading = false
                    }
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            isLoading = false
        }
    }

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            locationPermissionGranted = permissions.values.all { it }
            if (locationPermissionGranted) {
                updateLocation()
            }
        }

    LaunchedEffect(Unit) {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        when {
            permissions.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            } -> {
                locationPermissionGranted = true
                updateLocation()
            }
            else -> {
                requestPermissionLauncher.launch(permissions)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            currentLocation != null -> {
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(currentLocation!!, 12f)
                }


                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(
                        isMyLocationEnabled = locationPermissionGranted,
                        mapType = MapType.NORMAL
                    ),
                    uiSettings = MapUiSettings(
                        zoomControlsEnabled = true,
                        myLocationButtonEnabled = true
                    )
                ) {
                    Marker(
                        state = MarkerState(position = currentLocation!!),
                        title = "Lokasi Saya",
                        snippet = "Ini adalah lokasi saya yang sebenarnya"
                    )

                    merchants?.forEach { merchant ->
                        val lng = merchant.latitude?.toDoubleOrNull()
                        val lat = merchant.longitude?.toDoubleOrNull()

                        if (lat != null && lng != null &&
                            lat in -90.0..90.0 && lng in -180.0..180.0) {
                            val position = LatLng(lat, lng)
                            Log.d("MyLocationScreen", "Adding merchant marker at: $lat, $lng")
                            Marker(
                                state = MarkerState(position = position),
                                title = merchant.name ?: "Merchant",
                                snippet = merchant.description ?: "",
                                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                            )
                        } else {
                            Log.e("MyLocationScreen", "Invalid merchant coordinates: lat=$lat, lng=$lng")
                        }
                    }

                    markets?.forEach { market ->
                        val lng = market.latitude?.toDoubleOrNull()
                        val lat = market.longitude?.toDoubleOrNull()

                        if (lat != null && lng != null &&
                            lat in -90.0..90.0 && lng in -180.0..180.0) {
                            val position = LatLng(lat, lng) // Posisi yang benar
                            Log.d("MyLocationScreen", "Adding market marker at: $lat, $lng")
                            Marker(
                                state = MarkerState(position = position),
                                title = market.name ?: "Market",
                                snippet = market.description ?: "",
                                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                            )
                        } else {
                            Log.e("MyLocationScreen", "Invalid market coordinates: lat=$lat, lng=$lng")
                        }
                    }
                }
            }
        }

        MapLegend(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(end = 5.dp, top = 5.dp)
        )

        FloatingActionButton(
            onClick = {
                updateLocation()
                loadData(token)
            },
            containerColor = Color(0xFFED8A00), // Warna background FAB
            contentColor = Color.White, // Warna ikon
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Refresh, "Refresh Location")
        }



        Button(
            onClick = { navController.navigate("home") },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFED8A00))
        ) {
            Text("Back")
        }
    }
}

@Composable
fun MapLegend(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(16.dp)
            .width(200.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFED8A00))

    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Marked Place",
                fontFamily = sfui_semibold,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    "Your Location",
                    color = Color.White,
                    fontFamily = sfui_semibold
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.Store,
                    contentDescription = null,
                    tint = Color.Green,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    "Market",
                    color = Color.White,
                    fontFamily = sfui_semibold
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.Storefront,
                    contentDescription = null,
                    tint = Color.Blue,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    "Shop",
                    color = Color.White,
                    fontFamily = sfui_semibold
                )
            }
        }
    }
}