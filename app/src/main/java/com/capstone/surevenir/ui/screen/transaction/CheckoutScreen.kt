package com.capstone.surevenir.ui.screen.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.capstone.surevenir.R
import com.capstone.surevenir.data.network.response.CartItem
import com.capstone.surevenir.helper.formatCurrency
import com.capstone.surevenir.ui.screen.navmenu.sfui_bold
import com.capstone.surevenir.ui.screen.navmenu.sfui_med
import com.capstone.surevenir.ui.screen.navmenu.sfui_semibold
import com.capstone.surevenir.ui.viewmodel.CheckoutViewModel
import com.capstone.surevenir.ui.viewmodel.TokenViewModel

@Composable
fun CheckoutScreen(
    navController: NavController,
    checkoutViewModel: CheckoutViewModel = hiltViewModel(),
    tokenViewModel: TokenViewModel = hiltViewModel(),
) {
    val isLoading by checkoutViewModel.isLoading.collectAsState()
    val error by checkoutViewModel.error.collectAsState()
    val checkoutSuccess by checkoutViewModel.checkoutSuccess.collectAsState()
    val token by tokenViewModel.token.observeAsState()

    val selectedItems = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<List<CartItem>>("selectedItems") ?: emptyList()

    val totalPrice = selectedItems.sumOf { it.product.price * it.quantity }
    var showOrderDialog by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        if (token == null) {
            tokenViewModel.fetchToken()
        }
    }


    LaunchedEffect(checkoutSuccess) {
        if (checkoutSuccess) {
            navController.navigate("transaction") {
                popUpTo("transaction") { inclusive = true }
            }
            navController.currentBackStackEntry?.savedStateHandle?.set("selectedTab", 1)
        }
    }

    Scaffold(
        modifier = Modifier
            .background(Color.White),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(start = 20.dp, end = 30.dp, top = 20.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable(onClick = { navController.popBackStack() }),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow),
                        contentDescription = "Back",
                        modifier = Modifier.size(24.dp)
                    )
                }
                androidx.compose.material.Text(
                    text = "Checkout",
                    fontSize = 25.sp,
                    fontFamily = sfui_semibold,
                    color = Color(0xFFCC5B14)
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(selectedItems) { item ->
                        CheckoutItemCard(item)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth()
                        .background(Color.White)
                    ,
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Total Payment",
                                fontFamily = sfui_semibold,
                                fontSize = 16.sp,
                            )
                            Text(
                                text = formatCurrency(totalPrice),
                                fontFamily = sfui_semibold,
                                fontSize = 18.sp,
                                color = Color(0xFFED8A00)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { showOrderDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp),
                            enabled = !isLoading,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFED8A00)
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.White
                                )
                            } else {
                                Text(
                                    text = "ORDER",
                                    fontFamily = sfui_semibold,
                                    color = Color.White,
                                    fontSize = 20.sp
                                )
                            }
                        }
                    }
                }
            }

            if (showOrderDialog) {
                AlertDialog(
                    onDismissRequest = { showOrderDialog = false },
                    title = {
                        Text(
                            text = "Confirm Order",
                            fontFamily = sfui_bold,
                            fontSize = 18.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    },
                    text = {
                        val message = if (selectedItems.size == 1) {
                            val item = selectedItems.first()
                            "Are you sure you want to place the order for ${item.product.name}?"
                        } else {
                            "Are you sure you want to place the order for ${selectedItems.size} products?"
                        }
                        Text(
                            text = message,
                            fontFamily = sfui_med,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                token?.let { tokenValue ->
                                    checkoutViewModel.checkout("Bearer $tokenValue", selectedItems)
                                }
                                showOrderDialog = false
                            }
                        ) {
                            Text(
                                text = "Confirm",
                                fontFamily = sfui_semibold,
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showOrderDialog = false }
                        ) {
                            Text(
                                text = "Cancel",
                                fontFamily = sfui_semibold,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    },
                    containerColor = Color.White,
                    shape = MaterialTheme.shapes.medium,
                )
            }

            if (error != null) {
                AlertDialog(
                    onDismissRequest = { checkoutViewModel.clearError() },
                    title = { Text("Error") },
                    text = {
                        Text(error ?: "An unknown error occurred")
                    },
                    confirmButton = {
                        TextButton(onClick = { checkoutViewModel.clearError() }) {
                            Text("OK")
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun CheckoutItemCard(item: CartItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (item.product.images.isNotEmpty()) {
                AsyncImage(
                    model = item.product.images.first(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(6.dp))
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 20.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = item.product.name,
                    fontWeight = FontWeight.Bold,
                    fontFamily = sfui_semibold,
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.weight(1f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = formatCurrency(item.product.price),
                    color = Color.Gray,
                    fontSize = 12.sp,
                    fontFamily = sfui_med
                )
                Text(
                    text = "${item.quantity} items",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    fontFamily = sfui_med
                )
                Text(
                    text = "Subtotal: ${formatCurrency(item.product.price * item.quantity)}",
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontFamily = sfui_med
                )
            }
        }
    }
}