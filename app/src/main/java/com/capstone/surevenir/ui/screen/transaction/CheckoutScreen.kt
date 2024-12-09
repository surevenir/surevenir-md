package com.capstone.surevenir.ui.screen.transaction

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.capstone.surevenir.data.network.response.CartItem
import com.capstone.surevenir.ui.components.TopBar
import com.capstone.surevenir.ui.viewmodel.CheckoutViewModel
import com.capstone.surevenir.ui.viewmodel.TokenViewModel
import java.text.NumberFormat
import java.util.*

@Composable
fun CheckoutScreen(
    navController: NavController,
    checkoutViewModel: CheckoutViewModel = hiltViewModel(),
    tokenViewModel: TokenViewModel = hiltViewModel()
) {
    val isLoading by checkoutViewModel.isLoading.collectAsState()
    val error by checkoutViewModel.error.collectAsState()
    val checkoutSuccess by checkoutViewModel.checkoutSuccess.collectAsState()
    val token by tokenViewModel.token.observeAsState()

    val selectedItems = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<List<CartItem>>("selectedItems") ?: emptyList()

    val totalPrice = selectedItems.sumOf { it.product.price * it.quantity }


    LaunchedEffect(Unit) {
        if (token == null) {
            Log.d("CheckoutScreen", "Fetching token...")
            tokenViewModel.fetchToken()
        }
    }


    LaunchedEffect(checkoutSuccess) {
        if (checkoutSuccess) {
            Log.d("CheckoutScreen", "Checkout successful, navigating to transaction history")
            navController.navigate("transaction") {
                popUpTo("transaction") { inclusive = true }
            }
            navController.currentBackStackEntry?.savedStateHandle?.set("selectedTab", 1)
        }
    }

    LaunchedEffect(error) {
        error?.let { errorMessage ->
            Log.e("CheckoutScreen", "Checkout error: $errorMessage")
            // Handle error state
        }
    }


    Scaffold(
        topBar = {
            TopBar(
                title = "Checkout",
                onBackClick = { navController.navigateUp() }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
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

                // Total Payment Section
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
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
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = formatPrice(totalPrice),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                token?.let { tokenValue ->
                                    Log.d("CheckoutScreen", "Order button clicked")
                                    Log.d("CheckoutScreen", "Selected items: ${selectedItems.size}")
                                    checkoutViewModel.checkout("Bearer $tokenValue", selectedItems)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !isLoading
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            } else {
                                Text("Order")
                            }
                        }
                    }
                }
            }

            if (error != null) {
                AlertDialog(
                    onDismissRequest = { checkoutViewModel.clearError() },
                    title = { Text("Error") },
                    text = { Text(error ?: "An unknown error occurred") },
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
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (item.product.images.isNotEmpty()) {
                AsyncImage(
                    model = item.product.images.first(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(4.dp))
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = item.product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = formatPrice(item.product.price),
                    color = Color.Gray
                )
                Text(
                    text = "${item.quantity} items",
                    color = Color.Gray
                )
                Text(
                    text = "Subtotal: ${formatPrice(item.product.price * item.quantity)}",
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

private fun formatPrice(price: Int): String {
    return NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(price)
}