package com.capstone.surevenir.ui.screen.mylocation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.capstone.surevenir.data.network.response.MerchantData
import com.capstone.surevenir.model.Market
import com.capstone.surevenir.services.GeofencingService
import com.capstone.surevenir.ui.screen.navmenu.sfui_semibold
import com.capstone.surevenir.ui.viewmodel.*
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*

@Composable
fun MyLocationScreen(
    navController: NavController,
    merchantViewModel: MerchantViewModel = hiltViewModel(),
    marketViewModel: MarketViewModel = hiltViewModel(),
    tokenViewModel: TokenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val token by tokenViewModel.token.observeAsState()
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    var locationPermissionGranted by remember { mutableStateOf(false) }
    var backgroundLocationPermissionGranted by remember { mutableStateOf(false) }
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var merchants by remember { mutableStateOf<List<MerchantData>?>(null) }
    var markets by remember { mutableStateOf<List<Market>?>(null) }

    val backgroundPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        backgroundLocationPermissionGranted = isGranted
        if (!isGranted) {
            Toast.makeText(context, "Background location permission is required for geofencing.", Toast.LENGTH_LONG).show()
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        locationPermissionGranted = allGranted
        if (allGranted) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                backgroundPermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            }
            updateLocation(context, fusedLocationClient) { location ->
                currentLocation = location
                isLoading = false
            }
        }
    }


    fun loadData(currentToken: String?) {
        if (currentToken == null) return
        val bearerToken = "Bearer $currentToken"

        merchantViewModel.getMerchants(bearerToken) { merchantList ->
            merchants = merchantList
            setupGeofencing(context, merchantList, markets)
        }

        marketViewModel.getMarkets(bearerToken) { marketList ->
            markets = marketList
            setupGeofencing(context, merchants, marketList)
        }
    }

    LaunchedEffect(Unit) {
        checkAndRequestPermissions(context, locationPermissionLauncher)
        tokenViewModel.fetchToken()
    }

    LaunchedEffect(token) {
        if (token != null) loadData(token)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            currentLocation != null -> {
                GoogleMapContent(
                    currentLocation = currentLocation!!,
                    locationPermissionGranted = locationPermissionGranted,
                    merchants = merchants,
                    markets = markets,
                    navController = navController
                )
            }
        }

        MapLegend(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(end = 5.dp, top = 5.dp)
        )

        FloatingActionButton(
            onClick = {
                updateLocation(context, fusedLocationClient) { location ->
                    currentLocation = location
                }
                loadData(token)
            },
            containerColor = Color(0xFFED8A00),
            contentColor = Color.White,
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
private fun GoogleMapContent(
    currentLocation: LatLng,
    locationPermissionGranted: Boolean,
    merchants: List<MerchantData>?,
    markets: List<Market>?,
    navController: NavController
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLocation, 12f)
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
            state = MarkerState(position = currentLocation),
            title = "Lokasi Saya",
            snippet = "Ini adalah lokasi saya yang sebenarnya"
        )

        merchants?.forEach { merchant ->
            val lng = merchant.latitude?.toDoubleOrNull()
            val lat = merchant.longitude?.toDoubleOrNull()

            if (lat != null && lng != null && lat in -90.0..90.0 && lng in -180.0..180.0) {
                val position = LatLng(lat, lng)

                Circle(
                    center = position,
                    radius = 3000.0,
                    strokeColor = Color.Blue.copy(alpha = 0.5f),
                    fillColor = Color.Blue.copy(alpha = 0.1f),
                    strokeWidth = 2f
                )

                Marker(
                    state = MarkerState(position = position),
                    title = merchant.name ?: "Merchant",
                    snippet = merchant.description ?: "",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE),
                    onClick = {
                        navController.navigate("merchant/${merchant.id}")
                        true
                    }
                )
            }
        }

        markets?.forEach { market ->
            val lng = market.latitude?.toDoubleOrNull()
            val lat = market.longitude?.toDoubleOrNull()

            if (lat != null && lng != null && lat in -90.0..90.0 && lng in -180.0..180.0) {
                val position = LatLng(lat, lng)

                Circle(
                    center = position,
                    radius = 3000.0,
                    strokeColor = Color.Green.copy(alpha = 0.5f),
                    fillColor = Color.Green.copy(alpha = 0.1f),
                    strokeWidth = 2f
                )

                Marker(
                    state = MarkerState(position = position),
                    title = market.name ?: "Market",
                    snippet = market.description ?: "",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                )
            }
        }
    }
}

private fun setupGeofencing(context: Context, merchants: List<MerchantData>?, markets: List<Market>?) {
    merchants?.forEach { merchant ->
        val lng = merchant.latitude?.toDoubleOrNull()
        val lat = merchant.longitude?.toDoubleOrNull()

        if (lat != null && lng != null && lat in -90.0..90.0 && lng in -180.0..180.0) {
            Log.d("Geofencing", "Setting up merchant geofence at lat: $lat, lng: $lng")
            val intent = Intent(context, GeofencingService::class.java).apply {
                putExtra("id", "merchant_${merchant.id}")
                putExtra("lat", lat)
                putExtra("lng", lng)
                putExtra("type", "merchant")
                putExtra("name", merchant.name)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
    }

    markets?.forEach { market ->
        val lat = market.longitude?.toDoubleOrNull()
        val lng = market.latitude?.toDoubleOrNull()

        if (lat != null && lng != null && lat in -90.0..90.0 && lng in -180.0..180.0) {
            Log.d("Geofencing", "Setting up market geofence at lat: $lat, lng: $lng")
            val intent = Intent(context, GeofencingService::class.java).apply {
                putExtra("id", "market_${market.id}")
                putExtra("lat", lat)
                putExtra("lng", lng)
                putExtra("type", "market")
                putExtra("name", market.name)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        } else {
            Log.e("Geofencing", "Invalid market coordinates: lat=$lat, lng=$lng for market ${market.id}")
        }
    }
}

private fun checkAndRequestPermissions(
    context: Context,
    locationPermissionLauncher: ActivityResultLauncher<Array<String>>
) {
    val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    when {
        permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        } -> {
            locationPermissionLauncher.launch(permissions)
        }
        else -> {
            locationPermissionLauncher.launch(permissions)
        }
    }
}

private fun updateLocation(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    onLocationUpdate: (LatLng) -> Unit
) {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        return
    }

    try {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    onLocationUpdate(LatLng(location.latitude, location.longitude))
                }
            }
        }
    } catch (e: Exception) {
        Log.e("Location", "Error getting location", e)
    }
}

@Composable
fun MapLegend(modifier: Modifier = Modifier) {
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