package com.capstone.surevenir.ui.screen.navmenu

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.capstone.surevenir.data.network.response.CartData
import com.capstone.surevenir.data.network.response.CartItem
//import com.capstone.surevenir.ui.viewmodel.CartViewModel
import com.capstone.surevenir.ui.viewmodel.TokenViewModel
import kotlinx.coroutines.launch

@Composable
fun TransactionScreen(
    navController: NavHostController,
//    cartViewModel: CartViewModel = hiltViewModel(),
    tokenViewModel: TokenViewModel = hiltViewModel()
) {
//    val cartData by cartViewModel.cartData.collectAsState()
//    val isLoading by cartViewModel.isLoading.collectAsState()
//    val errorMessage by cartViewModel.errorMessage.collectAsState()
    val token by tokenViewModel.token.observeAsState()
    val scope = rememberCoroutineScope()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<CartItem?>(null) }

    LaunchedEffect(Unit) {
        if (token == null) {
            tokenViewModel.fetchToken()
        }
    }

    LaunchedEffect(token) {
        token?.let {
//            cartViewModel.getCart("Bearer $it")
        }
    }

    if (showDeleteDialog && itemToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Remove Item") },
            text = { Text("Are you sure you want to remove ${itemToDelete?.product?.name} from your cart?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        Log.d("TransactionScreen", "Delete button clicked")
                        val currentItemToDelete = itemToDelete
                        val currentToken = token
//
//                        if (currentToken != null && currentItemToDelete != null) {
//                            scope.launch {
//                                try {
////                                    cartViewModel.deleteCartItem(
//                                        "Bearer $currentToken",
//                                        currentItemToDelete.id
//                                    )
//                                } catch (e: Exception) {
//                                    Log.e("TransactionScreen", "Error launching delete", e)
//                                }
//                            }
//                        } else {
//                            Log.e(
//                                "TransactionScreen",
//                                "Token or ItemToDelete is null. Token: $currentToken, Item: $currentItemToDelete"
//                            )
//                        }
                        showDeleteDialog = false
                        itemToDelete = null
                    }
                ) {
                    Text("Remove")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    itemToDelete = null
                }) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Transaction",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

//        when {
////            isLoading -> {
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    CircularProgressIndicator()
//                }
//            }
//            errorMessage != null -> {
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(text = errorMessage!!)
//                }
//            }
//            cartData != null -> {
//                cartData?.let {
//                    CartContent(
//                        cartData = it,
//                        onDeleteClick = { cartItem ->
//                            itemToDelete = cartItem
//                            showDeleteDialog = true
//                        }
//                    )
//                }
//            }
//            cartData == null -> {
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(text = "No items in your cart.")
//                }
//            }
        }
    }
//}

@Composable
private fun CartContent(
    cartData: CartData,
    onDeleteClick: (CartItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(cartData.cart) { cartItem ->
            CartItemCard(
                cartItem = cartItem,
                onDeleteClick = onDeleteClick
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            TotalPriceCard(cartData.totalPrice)
        }
    }
}

@Composable
private fun CartItemCard(
    cartItem: CartItem,
    onDeleteClick: (CartItem) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (cartItem.product.images.isNotEmpty()) {
                AsyncImage(
                    model = cartItem.product.images.first(),
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
                    text = cartItem.product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "Rp ${cartItem.product.price}",
                    color = Color.Gray
                )
                Text(
                    text = "${cartItem.quantity} items",
                    color = Color.Gray
                )
                Text(
                    text = "Total: Rp ${cartItem.product.price * cartItem.quantity}",
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Added on ${cartItem.createdAt.substring(0, 10)}",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            IconButton(
                onClick = { onDeleteClick(cartItem) },
                modifier = Modifier.align(Alignment.Top)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete item",
                    tint = Color.Red
                )
            }
        }
    }
}

@Composable
private fun TotalPriceCard(totalPrice: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total Price",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = "Rp $totalPrice",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFFE65100)
            )
        }
    }
}