package com.capstone.surevenir.ui.screen.transaction

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.capstone.surevenir.data.network.response.CheckoutData
import com.capstone.surevenir.data.network.response.CheckoutDetail
import com.capstone.surevenir.ui.screen.navmenu.sfui_med
import com.capstone.surevenir.ui.screen.navmenu.sfui_semibold
import com.capstone.surevenir.helper.DateConverter
import com.capstone.surevenir.ui.screen.navmenu.sfui_bold
import com.capstone.surevenir.ui.viewmodel.ReviewsViewModel
import com.capstone.surevenir.ui.viewmodel.TokenViewModel


@Composable
fun DetailsCheckoutScreen(
    navController: NavController,
    checkoutData: CheckoutData,
    onRateClick: (Int) -> Unit,
    tokenViewModel: TokenViewModel = hiltViewModel()
) {
    val token by tokenViewModel.token.observeAsState()
    LaunchedEffect(Unit) {
        if (token == null) {
            Log.d("CheckoutScreen", "Fetching token...")
            tokenViewModel.fetchToken()
        }
    }

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

    var selectedTab by remember {
        mutableIntStateOf(savedStateHandle?.get<Int>("selectedTab") ?: 0)
    }
    LaunchedEffect(checkoutData.id) {
        savedStateHandle?.set("selectedTab", 1)
        selectedTab = 1
    }


    val groupedDetails = checkoutData.checkoutDetails.groupBy { it.product.merchant.name }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)

            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 12.dp)
                    ) {
                        Text(
                            text = "ORDER DETAILS",
                            fontFamily = sfui_bold,
                            fontSize = 22.sp,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = DateConverter().formatDateString(checkoutData.createdAt),
                            fontFamily = sfui_med,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        StatusIndicator(status = checkoutData.status)
                    }
                }
            }
        },
        bottomBar = {
            if (checkoutData.status == "PENDING") {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White),
                    shadowElevation = 8.dp,
                ) {
                    Button(
                        onClick = {
                            navController.navigate("shop")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFED8A00)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "ORDER AGAIN",
                            fontFamily = sfui_semibold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5)),
            contentPadding = PaddingValues(bottom = if (checkoutData.status == "COMPLETED") 80.dp else 16.dp)
        ) {
            groupedDetails.forEach { (merchantName, items) ->
                item {
                    Column {
                        HorizontalDivider(
                            modifier = Modifier
                                .padding(vertical = 12.dp)
                                .fillMaxWidth(),
                            thickness = 10.dp,
                            color = Color.LightGray
                        )
                        MerchantHeader(merchantName = merchantName, detail = items.first())
                    }
                }

                items(items) { detail ->
                    ProductItem(
                        detail = detail,
                        showRateButton = checkoutData.status == "COMPLETED",
                        onRateClick = { onRateClick(detail.productId) }
                    )

                    if (items.indexOf(detail) != items.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier
                                .padding(horizontal = 24.dp, vertical = 4.dp)
                                .fillMaxWidth(),
                            thickness = 2.dp,
                            color = Color.LightGray
                        )
                    }
                }
            }

            item {
                Column {
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(vertical = 12.dp)
                            .fillMaxWidth(),
                        thickness = 10.dp,
                        color = Color.LightGray
                    )

                    PaymentInformation(
                        merchantTotals = groupedDetails.map {
                            it.key to it.value.sumOf { detail -> detail.productSubtotal }.toInt()
                        },
                        totalAmount = checkoutData.totalPrice,
                        totalProducts = checkoutData.checkoutDetails.sumOf { it.productQuantity }
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusIndicator(status: String) {
    val (backgroundColor, textColor) = when (status) {
        "COMPLETED" -> Color(0xFF86EFAC) to Color(0xFF166534)
        "PENDING" -> Color(0xFFFDE047) to Color(0xFF854D0E)
        else -> Color(0xFFFCA5A5) to Color(0xFF991B1B)
    }

    Text(
        text = status,
        fontFamily = sfui_semibold,
        fontSize = 12.sp,
        color = textColor,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
private fun MerchantHeader(merchantName: String, detail: CheckoutDetail) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 30.dp, end = 30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = detail.product.merchant.profile_image_url,
                contentDescription = "Store Avatar",
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = merchantName,
                fontFamily = sfui_semibold,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ProductItem(
    detail: CheckoutDetail,
    showRateButton: Boolean,
    reviewViewModel: ReviewsViewModel = hiltViewModel(),
    onRateClick: () -> Unit
) {
    val isRated by remember(detail.productId) {
        derivedStateOf { reviewViewModel.isProductRated(detail.productId) }
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 24.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product Image
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF5F5F5))
            ) {
                AsyncImage(
                    model = detail.product.images.firstOrNull()?.url,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Product Details
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                // Product Name
                Text(
                    text = detail.product.name,
                    fontFamily = sfui_semibold,
                    fontSize = 16.sp,
                    color = Color.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Rp ${detail.productPrice.toInt()}",
                        fontFamily = sfui_med,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "(${detail.productQuantity} items)",
                        fontFamily = sfui_med,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Subtotal:",
                    fontFamily = sfui_med,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Rp ${detail.productSubtotal.toInt()}",
                        fontFamily = sfui_semibold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )

                    if (showRateButton) {
                        Button(
                            onClick = onRateClick,
                            enabled = !isRated,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isRated) Color.Gray else Color(0xFFED8A00)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .height(36.dp)
                                .width(80.dp)
                        ) {
                            Text(
                                text = if (isRated) "Rated" else "Rate",
                                fontFamily = sfui_semibold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentInformation(
    merchantTotals: List<Pair<String, Int>>,
    totalAmount: Int,
    totalProducts: Int
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 30.dp, vertical = 24.dp)
        ) {
            Text(
                text = "Payment Information",
                fontFamily = sfui_semibold,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            merchantTotals.forEach { (merchantName, total) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = merchantName,
                        fontFamily = sfui_med,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Rp $total",
                        fontFamily = sfui_med,
                        fontSize = 14.sp
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                thickness = 2.dp,
                color = Color.LightGray
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Total Payment:",
                        fontFamily = sfui_semibold,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "($totalProducts Products)",
                        fontFamily = sfui_med,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                Text(
                    text = "Rp $totalAmount",
                    fontFamily = sfui_semibold,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFED8A00)
                )
            }
        }
    }
}