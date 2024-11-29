package com.capstone.surevenir.ui.screen.singlescreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.capstone.surevenir.R
import com.capstone.surevenir.model.Product
import com.capstone.surevenir.ui.screen.navmenu.ProductsSection
import com.capstone.surevenir.ui.screen.navmenu.sfui_med
import com.capstone.surevenir.ui.screen.navmenu.sfui_semibold
import com.capstone.surevenir.ui.viewmodel.MerchantDetailViewModel
import com.capstone.surevenir.ui.viewmodel.MerchantViewModel
import com.capstone.surevenir.ui.viewmodel.TokenViewModel

@Composable
fun SingleShopScreen(
    merchantId: Int,
    navController: NavHostController,
    tokenViewModel: TokenViewModel = hiltViewModel(),
    merchantViewModel: MerchantDetailViewModel = hiltViewModel()
) {

    val token by tokenViewModel.token.observeAsState()
    val merchantDetail by merchantViewModel.merchantDetail.collectAsState()
    val error by merchantViewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        tokenViewModel.fetchToken()
    }

    LaunchedEffect(merchantId, token) {
        if (token != null) {
            Log.d("SingleProductScreen Debug", "Token is available: $token")
            merchantViewModel.fetchMerchantDetail(merchantId, token!!)
        } else {
            Log.d("SingleProductScreen Debug", "Token is still null")
        }
    }


    if (merchantDetail != null) {
        val merchant = merchantDetail!!
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        )
        {
            item {
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
                        text = "Detail Shop",
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
                        .padding(16.dp)
                ) {
                    // Gambar Toko
                    AsyncImage(
                        model = merchant.profileImageUrl ?: "https://via.placeholder.com/150",
                        contentDescription = merchant.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Nama Toko
                    Text(
                        text = merchant.name,
                        fontFamily = sfui_semibold,
                        fontSize = 24.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Deskripsi
                    Text(
                        text = merchant.description,
                        fontFamily = sfui_med,
                        fontSize = 16.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Lokasi (Latitude dan Longitude)
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_location),
                            contentDescription = "Location Icon",
                            tint = Color(0xFFCC5B14),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${merchant.latitude}, ${merchant.longitude}",
                            fontFamily = sfui_med,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
        }
}