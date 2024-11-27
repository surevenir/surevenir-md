package com.capstone.surevenir.ui.splash


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.capstone.surevenir.model.Category
import com.capstone.surevenir.model.Shop
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@Composable
fun SingleProductScreen(navController: NavHostController) {

    val images = listOf(
        R.drawable.product_image,
        R.drawable.product_image,
        R.drawable.product_image,
        R.drawable.product_image
    )


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE7E7E9))
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
                    text = "Detail Souvenir",
                    fontSize = 25.sp,
                    fontFamily = sfui_semibold,
                    color = Color(0xFFCC5B14)
                )
            }
        }


        item {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                ImageSlider(images = images)

                Spacer(modifier = Modifier.height(16.dp))

            }
        }


        item {
            Column {
                Text(
                    text = "Rp. 150.000",
                    fontSize = 24.sp,
                    fontFamily = sfui_semibold,
                    color = Color.Black
                )
                Text(
                    text = "Bali Hand Magnet",
                    fontSize = 18.sp,
                    fontFamily = sfui_semibold,
                    color = Color.Black
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_star),
                        contentDescription = "Rating Star",
                        tint = Color(0xFFFFCC00),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "4.8 (7)",
                        fontSize = 14.sp,
                        fontFamily = sfui_text,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                }
            }
        }


        item {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = "Details",
                    fontSize = 18.sp,
                    fontFamily = sfui_semibold,
                    color = Color.Black
                )
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(
                        text = "Category: ",
                        fontSize = 14.sp,
                        fontFamily = sfui_text,
                        color = Color.Gray
                    )
                    Text(
                        text = "Magnet",
                        fontSize = 14.sp,
                        fontFamily = sfui_text,
                        color = Color(0xFFFF6600) // Orange
                    )
                }
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(
                        text = "Weight: ",
                        fontSize = 14.sp,
                        fontFamily = sfui_text,
                        color = Color.Gray
                    )
                    Text(
                        text = "500 g",
                        fontSize = 14.sp,
                        fontFamily = sfui_text,
                        color = Color.Black
                    )
                }
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(
                        text = "Condition: ",
                        fontSize = 14.sp,
                        fontFamily = sfui_text,
                        color = Color.Gray
                    )
                    Text(
                        text = "New",
                        fontSize = 14.sp,
                        fontFamily = sfui_text,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                }
            }
        }


        item {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = "Deskripsi produk",
                    fontSize = 18.sp,
                    fontFamily = sfui_semibold,
                    color = Color.Black
                )
                Text(
                    text = "The Bali Hand Magnet captures Bali’s vibrant culture through intricate hand gestures inspired by traditional dance. Made with detailed craftsmanship, these colorful magnets are perfect keepsakes, adding a touch of Balinese artistry to any metal surface.",
                    fontSize = 14.sp,
                    fontFamily = sfui_text,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }


        item {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = "Reviews",
                    fontSize = 18.sp,
                    fontFamily = sfui_semibold,
                    color = Color.Black
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_star),
                        contentDescription = "Rating Star",
                        tint = Color(0xFFFFCC00),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "4.8/5.0 (7 review)",
                        fontSize = 14.sp,
                        fontFamily = sfui_text,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                repeat(2) {
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        Text(
                            text = "Abdul",
                            fontSize = 16.sp,
                            fontFamily = sfui_semibold,
                            color = Color.Black
                        )
                        Text(
                            text = "20 April 2024",
                            fontSize = 12.sp,
                            fontFamily = sfui_text,
                            color = Color.Gray
                        )
                        Text(
                            text = "The Bali Hand Magnet captures Bali’s vibrant culture through intricate hand gestures inspired by traditional dance.",
                            fontSize = 14.sp,
                            fontFamily = sfui_text,
                            color = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Text(
                    text = "See All Reviews",
                    fontSize = 14.sp,
                    fontFamily = sfui_semibold,
                    color = Color(0xFFFF6600),
                    modifier = Modifier.clickable { /* Navigate to All Reviews */ }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageSlider(images: List<Int>) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { images.size })
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Image(
                painter = painterResource(id = images[page]),
                contentDescription = "Product Image",
                modifier = Modifier.fillMaxSize()
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(images.size) { index ->
                Box(
                    modifier = Modifier
                        .size(if (pagerState.currentPage == index) 10.dp else 8.dp)
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(
                            if (pagerState.currentPage == index) Color.White else Color.Gray
                        )
                        .clickable {
                            coroutineScope.launch {
                                pagerState.scrollToPage(index)
                            }
                        }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSingleProductScreen() {
    val navController = rememberNavController()
    SingleProductScreen(navController = navController)
}