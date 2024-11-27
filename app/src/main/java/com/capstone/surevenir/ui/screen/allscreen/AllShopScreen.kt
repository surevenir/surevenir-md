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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.capstone.surevenir.ui.component.ShopCard
import com.capstone.surevenir.ui.screen.navmenu.sfui_semibold

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AllShopScreen(navController: NavHostController) {
    val merchantLists = listOf(
        Merchant(R.drawable.shop, "Ketut Art", "Ubud", 10),
        Merchant(R.drawable.shop, "Sukawati Market", "Sukawati", 20),
        Merchant(R.drawable.shop, "Ubud Handicraft", "Ubud", 15),
        Merchant(R.drawable.shop, "Bali Crafts", "Denpasar", 12),
        Merchant(R.drawable.shop, "Art Shop", "Kuta", 8),
        Merchant(R.drawable.shop, "Handmade Store", "Seminyak", 14)
    )

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

        ShopSectionAll(merchants = merchantLists, navController)
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
            ShopCard(
                imageRes = shop.imageRes,
                shopName = shop.shopName,
                shopLocation = shop.shopLocation,
                totalShopProduct = shop.totalShopProduct,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(8.dp)
                    .clickable { navController.navigate("singleShop") }
            )
        }
    }
}