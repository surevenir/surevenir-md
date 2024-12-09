package com.capstone.surevenir.ui.screen.allscreen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

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

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.capstone.surevenir.BuildConfig
import com.capstone.surevenir.model.Market
import com.capstone.surevenir.ui.components.MarketCard
import com.capstone.surevenir.ui.screen.navmenu.sfui_semibold
import com.capstone.surevenir.ui.viewmodel.GeocodingViewModel
import com.capstone.surevenir.ui.viewmodel.MarketViewModel
import com.capstone.surevenir.ui.viewmodel.TokenViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

@Composable
fun AllMarket(navController: NavHostController, marketViewModel: MarketViewModel = hiltViewModel(), tokenViewModel : TokenViewModel = hiltViewModel()) {
    val markets = remember { mutableStateOf<List<Market>?>(null) }
    val token by tokenViewModel.token.observeAsState()

    LaunchedEffect(Unit) {
        tokenViewModel.fetchToken()
    }

    if (token != null) {
        LaunchedEffect(token) {
            marketViewModel.getMarkets("Bearer $token") { marketList ->
                markets.value = marketList
                Log.d("ShopScreen", "Markets fetched: $marketList")
            }
        }
    } else {
        Log.d("ShopScreen", "Token is null")
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
                text = "All Markets",
                fontSize = 25.sp,
                fontFamily = sfui_semibold,
                color = Color(0xFFCC5B14)
            )
        }

        MarketSectionDown(markets = markets.value ?: emptyList(), navController)
    }
}

@Composable
fun MarketSectionDown(
    markets: List<Market>,
    navController: NavController,
    geocodingViewModel: GeocodingViewModel = hiltViewModel()
) {
    val updatedMarkets = remember(markets) { mutableStateOf(markets) }
    val isLoading = remember { mutableStateOf(true) }
    val apiKey = BuildConfig.MAPS_API_KEY
    val remainingRequests = remember { mutableStateOf(markets.size) }

    Log.d("MarketSection", "Initial markets size: ${markets.size}")

    LaunchedEffect(markets) {
        Log.d("MarketSection", "LaunchedEffect triggered. Markets size: ${markets.size}")
        isLoading.value = true

        if (markets.isEmpty()) {
            Log.d("MarketSection", "No markets to process, setting loading to false")
            isLoading.value = false
            return@LaunchedEffect
        }

        markets.forEach { market ->
            Log.d("MarketValidation", "Processing market - ID: ${market.id}, Latitude: ${market.latitude}, Longitude: ${market.longitude}")
            val latitude = market.latitude?.toDoubleOrNull()
            val longitude = market.longitude?.toDoubleOrNull()

            if (latitude != null && longitude != null && latitude in -90.0..90.0 && longitude in -180.0..180.0) {
                geocodingViewModel.getSubDistrictFromCoordinates(longitude, latitude, apiKey) { subDistrict ->
                    Log.d("GeocodingResult", "Received subDistrict for Market ID ${market.id}: $subDistrict")

                    val currentMarkets = updatedMarkets.value.toMutableList()
                    val updatedIndex = currentMarkets.indexOfFirst { it.id == market.id }

                    if (updatedIndex != -1) {
                        val updatedMarket = currentMarkets[updatedIndex].copy(
                            marketLocation = subDistrict ?: "No Location"
                        )
                        currentMarkets[updatedIndex] = updatedMarket
                        updatedMarkets.value = currentMarkets

                        Log.d("MarketSection", "Updated market ${market.id} location to: ${subDistrict}")
                    }

                    remainingRequests.value -= 1
                    Log.d("MarketSection", "Remaining requests: ${remainingRequests.value}")

                    if (remainingRequests.value <= 0) {
                        Log.d("MarketSection", "All requests completed, setting loading to false")
                        isLoading.value = false
                    }
                }
            } else {
                Log.e("MarketSection", "Invalid coordinates for market ${market.id}")
                remainingRequests.value -= 1
                if (remainingRequests.value <= 0) {
                    isLoading.value = false
                }
            }
        }
    }

    if (isLoading.value) {
        SkeletonLoadingView()
    } else {
        if (updatedMarkets.value.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No markets available")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp)
            ) {
                items(
                    items = updatedMarkets.value,
                    key = { it.id }
                ) { market ->
                    Log.d("MarketSection", "Rendering market card - ID: ${market.id}, Name: ${market.name}, Location: ${market.marketLocation}")
                    Log.d("MarketCardImage", "Profile image URL: ${market.profileImageUrl}")
                    MarketCard(
                        imageRes = market.profileImageUrl ?: "https://via.placeholder.com/150",
                        marketName = market.name ?: "Unknown Name",
                        marketLocation = market.marketLocation ?: "No Location",
                        marketDescription = market.description ?: "No description available",
                        modifier = Modifier
                            .height(225.dp)
                            .fillMaxWidth()
                            .padding(8.dp)
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .clickable {
                                navController.navigate("market/${market.id}")
                            }

                    )
                }
            }
        }
    }
}

@Composable
fun SkeletonLoadingView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        SkeletonCard()

        SkeletonCard()
    }
}

@Composable
fun SkeletonCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray)
            .shimmering()
    )
}

@Composable
fun Modifier.shimmering(): Modifier {
    return this.then(Modifier.placeholder(visible = true, highlight = PlaceholderHighlight.shimmer()))
}




