package com.capstone.surevenir.ui.screen.allscreen

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.capstone.surevenir.ui.component.ShopCard
import com.capstone.surevenir.ui.screen.navmenu.sfui_semibold
import com.capstone.surevenir.ui.viewmodel.MerchantViewModel
import com.capstone.surevenir.ui.viewmodel.TokenViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AllShopScreen(navController: NavHostController, merchantViewModel: MerchantViewModel = hiltViewModel(), tokenViewModel : TokenViewModel = hiltViewModel()) {
    val merchants = remember { mutableStateOf<List<Merchant>?>(null) }
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

        ShopSectionAll(merchants = merchants.value ?: emptyList(), navController)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShopSectionAll(merchants: List<Merchant>, navController: NavHostController) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(8.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(merchants) { shop ->
            shop.profile_image_url?.let {
                ShopCard(
                    imageRes = it,
                    shopName = shop.name,
                    shopLocation = "${shop.latitude}, ${shop.longitude}",
                    totalShopProduct = shop.products_count,
                    modifier = Modifier
                        .width(200.dp)
                        .padding(end = 10.dp)
                        .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .clickable { navController.navigate("merchant/${shop.id}") }
                )
            }
        }
    }
}