package com.capstone.surevenir.ui.screen.allscreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.capstone.surevenir.R
import com.capstone.surevenir.model.Merchant

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.capstone.surevenir.BuildConfig
import com.capstone.surevenir.data.network.response.MerchantData
import com.capstone.surevenir.ui.component.ShopCard
import com.capstone.surevenir.ui.screen.navmenu.sfui_semibold
import com.capstone.surevenir.ui.viewmodel.GeocodingViewModel
import com.capstone.surevenir.ui.viewmodel.MerchantViewModel
import com.capstone.surevenir.ui.viewmodel.TokenViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AllShopScreen(navController: NavHostController, merchantViewModel: MerchantViewModel = hiltViewModel(), tokenViewModel : TokenViewModel = hiltViewModel()) {
    val merchants = remember { mutableStateOf<List<MerchantData>?>(null) }
    val token by tokenViewModel.token.observeAsState()

    LaunchedEffect(Unit) {
        tokenViewModel.fetchToken()
    }

    if (token != null) {
        LaunchedEffect(token) {
            merchantViewModel.getMerchants("Bearer $token") { merchantList ->
                merchants.value = merchantList
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFE7E7E9), CircleShape)
                    .clickable(onClick = { navController.popBackStack() }),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow),
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = "All Shop",
                fontSize = 25.sp,
                fontFamily = sfui_semibold,
                color = Color(0xFFCC5B14)
            )
        }

        ShopSectionAll(shops = merchants.value ?: emptyList(), navController)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShopSectionAll(shops: List<MerchantData>, navController: NavHostController, geocodingViewModel: GeocodingViewModel = hiltViewModel()) {
    val updatedShops = remember(shops) { mutableStateOf(shops) }
    val isLoading = remember { mutableStateOf(true) }
    val apiKey = BuildConfig.MAPS_API_KEY
    val remainingRequests = remember { mutableStateOf(shops.size) }

    Log.d("ShopSection", "Initial shops size: ${shops.size}")

    LaunchedEffect(shops) {
        Log.d("ShopSection", "LaunchedEffect triggered. Shops size: ${shops.size}")
        isLoading.value = true

        if (shops.isEmpty()) {
            Log.d("ShopSection", "No shops to process, setting loading to false")
            isLoading.value = false
            return@LaunchedEffect
        }

        shops.forEach { shop ->
            Log.d("ShopValidation", "Processing shop - ID: ${shop.id}, Latitude: ${shop.latitude}, Longitude: ${shop.longitude}")
            val latitude = shop.longitude?.toDoubleOrNull()
            val longitude = shop.latitude?.toDoubleOrNull()

            if (latitude != null && longitude != null && latitude in -90.0..90.0 && longitude in -180.0..180.0) {
                geocodingViewModel.getSubDistrictFromCoordinates(latitude, longitude, apiKey) { subDistrict ->
                    Log.d("GeocodingResult", "Received subDistrict for Shop ID ${shop.id}: $subDistrict")

                    val currentShops = updatedShops.value.toMutableList()
                    val updatedIndex = currentShops.indexOfFirst { it.id == shop.id }

                    if (updatedIndex != -1) {
                        val updatedShop = currentShops[updatedIndex].copy(
                            location = subDistrict ?: "No Location"
                        )
                        currentShops[updatedIndex] = updatedShop
                        updatedShops.value = currentShops

                        Log.d("ShopSection", "Updated shop ${shop.id} location to: ${subDistrict}")
                    }

                    remainingRequests.value -= 1
                    Log.d("ShopSection", "Remaining requests: ${remainingRequests.value}")

                    if (remainingRequests.value <= 0) {
                        Log.d("ShopSection", "All requests completed, setting loading to false")
                        isLoading.value = false
                    }
                }
            } else {
                Log.e("ShopSection", "Invalid coordinates for shop ${shop.id}")
                remainingRequests.value -= 1
                if (remainingRequests.value <= 0) {
                    isLoading.value = false
                }
            }
        }
    }

    if (isLoading.value) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(8.dp))
                androidx.compose.material3.Text("Loading shop locations...")
            }
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(
                items = updatedShops.value,
                key = { it.id }
            ) { shop ->
                shop.profile_image_url?.let {
                    Log.d("ShopSection", "Rendering shop card - ID: ${shop.id}, Name: ${shop.name}, Location: ${shop.location}")
                    ShopCard(
                        imageRes = it,
                        shopName = shop.name,
                        shopLocation = shop.location ?: "No Location",
                        totalShopProduct = shop.products_count,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .clickable { navController.navigate("merchant/${shop.id}") }
                    )
                }
            }
        }
    }
}
