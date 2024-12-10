package com.capstone.surevenir.ui.screen.singlescreen

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
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
import com.capstone.surevenir.ui.viewmodel.TokenViewModel

@Composable
fun SingleShopScreen(
    merchantId: Int,
    navController: NavHostController,
//    location : String,
//    productTotal: Int,
    tokenViewModel: TokenViewModel = hiltViewModel(),
    merchantViewModel: MerchantDetailViewModel = hiltViewModel(),
    productViewModel: ProductViewModel = hiltViewModel()
) {
    val token by tokenViewModel.token.observeAsState()
    val merchantDetail by merchantViewModel.merchantDetail.collectAsState()
    val error by merchantViewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        tokenViewModel.fetchToken()
    }

    val shopData = navController.previousBackStackEntry?.savedStateHandle?.get<ShopData>("shopData")

    LaunchedEffect(merchantId, token) {
        if (token != null) {
            Log.d("SingleProductScreen Debug", "Token is available: $token")
            merchantViewModel.fetchMerchantDetail(merchantId, token!!)
        } else {
            Log.d("SingleProductScreen Debug", "Token is still null")
        }
    }

    val launchGoogleMapsIntent = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {  }
    )




    val shopDataTemp = remember { mutableStateOf(ShopData("", 0)) }

    LaunchedEffect(merchantId) {
        shopDataTemp.value = shopData ?: ShopData("No Location", 0)
        if (token != null) {
            merchantViewModel.fetchMerchantDetail(merchantId, token!!)
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
                            .clickable {
                                navController.previousBackStackEntry?.savedStateHandle?.set(
                                    "shopData",
                                    shopDataTemp.value
                                )
                                navController.popBackStack()
                            },
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
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = merchant.profileImageUrl ?: "https://via.placeholder.com/150",
                        contentDescription = merchant.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(10000000.dp))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = merchant.name,
                        fontFamily = sfui_semibold,
                        fontSize = 24.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = merchant.description,
                        fontFamily = sfui_text,
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(end = 10.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_location),
                                contentDescription = "Location Icon",
                                tint = Color(0xFFCC5B14),
                                modifier = Modifier.size(30.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${shopDataTemp.value.location}",
                                fontFamily = sfui_med,
                                fontSize = 18.sp,
                                color = Color.Black
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_souvenir),
                                contentDescription = "Location Icon",
                                tint = Color(0xFFCC5B14),
                                modifier = Modifier.size(30.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${shopDataTemp?.value?.productsCount}" ,
                                fontFamily = sfui_med,
                                fontSize = 18.sp,
                                color = Color.Black
                            )
                        }

                    }

                }
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                        },
                        modifier = Modifier
                            .width(300.dp)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFED8A00)),
                        shape = RoundedCornerShape(20.dp),
                    ) {
                        Text(
                            text = "View On google Maps ↗",
                            color = Color.White,
                            fontFamily = sfui_semibold,
                            modifier = Modifier
                                .clickable {
                                    val gmmIntentUri = Uri.parse("geo:0,0?q=${merchant.longitude},${merchant.latitude}")
                                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                    mapIntent.setPackage("com.google.android.apps.maps")
                                    launchGoogleMapsIntent.launch(mapIntent)
                                }
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .padding(top = 30.dp)
                            .fillMaxWidth()
                            .height(0.4.dp)
                            .background(Color.Black)
                    )
                }
            }
            item {
                MerchantProductsSection(
                    merchantId = merchantId,
                    productViewModel = productViewModel,
                    navController = navController
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun MerchantProductsSection(
    merchantId: Int,
    productViewModel: ProductViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val merchantProducts by productViewModel.merchantProducts.collectAsState()

    LaunchedEffect(merchantId) {
        productViewModel.getProductsByMerchantId(merchantId)
    }

    Column(modifier = modifier) {
        Text(
            text = "Products",
            fontFamily = sfui_semibold,
            fontSize = 20.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        if (merchantProducts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No products exist yet from this shop",
                    fontFamily = sfui_med,
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            merchantProducts.chunked(2).forEach { rowProducts ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    rowProducts.forEach { product ->
                        ProductCard(
                            product = product,
                            modifier = Modifier
                                .weight(1f)
                                .height(265.dp)
                                .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
                                .clickable {
                                    navController.navigate("product/${product.id}")
                                }
                        )
                    }
                    if (rowProducts.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}