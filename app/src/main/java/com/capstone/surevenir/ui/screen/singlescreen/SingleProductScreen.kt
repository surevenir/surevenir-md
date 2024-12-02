package com.capstone.surevenir.ui.screen.singlescreen


import android.util.Log
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

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.capstone.surevenir.helper.formatPrice
import com.capstone.surevenir.ui.components.ProductCard
import com.capstone.surevenir.model.Product
import com.capstone.surevenir.ui.screen.navmenu.sfui_med
import com.capstone.surevenir.ui.screen.navmenu.sfui_semibold
import com.capstone.surevenir.ui.screen.navmenu.sfui_text
import com.capstone.surevenir.ui.viewmodel.ProductDetailViewModel
import com.capstone.surevenir.ui.viewmodel.TokenViewModel
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@Composable
fun SingleProductScreen(
    productId: Int,
    navController: NavController,
    tokenViewModel: TokenViewModel = hiltViewModel(),
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    val token by tokenViewModel.token.observeAsState()
    val productDetail by viewModel.productDetail.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        tokenViewModel.fetchToken()
    }

    LaunchedEffect(productId, token) {
        if (token != null) {
            Log.d("SingleProductScreen Debug", "Token is available: $token")
            viewModel.fetchProductDetail(productId, token!!)
        } else {
            Log.d("SingleProductScreen Debug", "Token is still null")
        }
    }


    if (productDetail != null) {
        val product = productDetail!!
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE7E7E9))
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
                    Spacer(modifier = Modifier.height(10.dp))
                    ImageSlider(images = product.images.map { it.url })
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        "Rp ${formatPrice(product.price)}",
                        fontSize = 24.sp,
                        fontFamily = sfui_semibold,
                        color = Color.Black
                    )
                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.Gray)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = product.name,
                        fontSize = 25.sp,
                        fontFamily = sfui_semibold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = product.description,
                        fontSize = 14.sp,
                        fontFamily = sfui_text,
                        color = Color.Gray
                    )
                }
            }

            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(15.dp)
                        .background(Color.LightGray)
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Details",
                        fontSize = 25.sp,
                        fontFamily = sfui_semibold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row {
                        Text(
                            text = "Category: ",
                            fontSize = 18.sp,
                            fontFamily = sfui_text,
                            color = Color.Gray
                        )
                        Text(
                            text = product.product_categories.joinToString(", ") { it.name },
                            fontSize = 18.sp,
                            fontFamily = sfui_med,
                            color = Color(0xFFFF6600)
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .padding(top = 15.dp)
                            .fillMaxWidth()
                            .height(0.4.dp)
                            .background(Color.Gray)
                    )
                }
            }

            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(15.dp)
                        .background(Color.LightGray)
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.shop),
                        contentDescription = "Shop Image",
                        modifier = Modifier
                            .size(80.dp)
                            .background(Color.White)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = product.merchant.name,
                            fontFamily = sfui_semibold,
                            fontSize = 18.sp,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_location),
                                contentDescription = "Location Icon",
                                tint = Color(0xFFCC5B14),
                                modifier = Modifier.size(16.dp)
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                text = product.merchant.latitude.toString(),
                                fontFamily = sfui_med,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    } else if (error != null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Error: $error", color = Color.Red)
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Loading...", style = MaterialTheme.typography.h6)
        }
    }
}

@Composable
fun ImageSlider(images: List<String>) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { images.size })
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Image(
                painter = rememberImagePainter(data = images[page]),
                contentDescription = "Product Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
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

