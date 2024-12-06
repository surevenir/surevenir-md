package com.capstone.surevenir.ui.screen.singlescreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.capstone.surevenir.ui.viewmodel.MarketViewModel
import com.capstone.surevenir.ui.viewmodel.TokenViewModel

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.capstone.surevenir.R
import com.capstone.surevenir.model.Product
import com.capstone.surevenir.model.ShopData
import com.capstone.surevenir.ui.components.ProductCard
import com.capstone.surevenir.ui.screen.navmenu.ProductsSection
import com.capstone.surevenir.ui.screen.navmenu.sfui_med
import com.capstone.surevenir.ui.screen.navmenu.sfui_semibold
import com.capstone.surevenir.ui.screen.navmenu.sfui_text
import com.capstone.surevenir.ui.viewmodel.MerchantDetailViewModel
import com.capstone.surevenir.ui.viewmodel.MerchantViewModel
import com.capstone.surevenir.ui.viewmodel.ProductViewModel

@Composable
fun SingleMarketScreen(
    marketId: Int,
    navController: NavHostController,
    tokenViewModel: TokenViewModel = hiltViewModel(),
    marketViewModel: MarketViewModel = hiltViewModel()
) {
    val token by tokenViewModel.token.observeAsState()
    val marketDetail by marketViewModel.marketDetail.collectAsState()
    val isLoading by marketViewModel.isLoading.collectAsState()
    val error by marketViewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        tokenViewModel.fetchToken()
    }

    LaunchedEffect(token) {
        token?.let {
            marketViewModel.fetchMarketDetail(marketId, it)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFFCC5B14)
                )
            }
            error != null -> {
                Text(
                    text = error ?: "Unknown error occurred",
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }
            marketDetail != null -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(Color(0xFFE7E7E9), CircleShape)
                                    .clickable { navController.popBackStack() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.arrow),
                                    contentDescription = "Back",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Text(
                                text = "Market Detail",
                                fontSize = 25.sp,
                                fontFamily = sfui_semibold,
                                color = Color(0xFFCC5B14)
                            )
                        }
                    }
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                model = marketDetail?.profile_image_url,
                                contentDescription = marketDetail?.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(16.dp))
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = marketDetail?.name ?: "",
                                fontFamily = sfui_semibold,
                                fontSize = 24.sp,
                                color = Color.Black
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = marketDetail?.description ?: "",
                                fontFamily = sfui_text,
                                fontSize = 16.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = {
                                    val latitude = marketDetail?.latitude
                                    val longitude = marketDetail?.longitude
                                    val uri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude")
                                    val intent = Intent(Intent.ACTION_VIEW, uri)
                                    intent.setPackage("com.google.android.apps.maps")
                                    navController.context.startActivity(intent)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFED8A00)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 32.dp)
                            ) {
                                Text(
                                    text = "View on Google Maps ↗",
                                    color = Color.White,
                                    fontFamily = sfui_semibold
                                )
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Avalaible Shops",
                            fontSize = 25.sp,
                            fontFamily = sfui_semibold,
                            color = Color(0xFFCC5B14)
                        )
                    }
                }
            }
        }
    }
}