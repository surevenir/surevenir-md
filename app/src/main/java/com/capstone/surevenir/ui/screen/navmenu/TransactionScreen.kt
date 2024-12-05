package com.capstone.surevenir.ui.screen.navmenu

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
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
import com.capstone.surevenir.ui.components.CheckoutHistoryCard
import com.capstone.surevenir.ui.viewmodel.CartViewModel
import com.capstone.surevenir.ui.viewmodel.CheckoutViewModel
import com.capstone.surevenir.ui.viewmodel.TokenViewModel
import kotlinx.coroutines.launch

@Composable
fun TransactionScreen(
    navController: NavHostController,
    cartViewModel: CartViewModel = hiltViewModel(),
    checkoutViewModel: CheckoutViewModel = hiltViewModel(),
    tokenViewModel: TokenViewModel = hiltViewModel()
) {
    val cartData by cartViewModel.cartData.collectAsState()
    val isLoading by cartViewModel.isLoading.collectAsState()
    val errorMessage by cartViewModel.errorMessage.collectAsState()
    val token by tokenViewModel.token.observeAsState()
    val checkoutSuccess by checkoutViewModel.checkoutSuccess.collectAsState()
    val scope = rememberCoroutineScope()

    var selectedItems by remember { mutableStateOf(setOf<Int>()) }
    var showTab by remember { mutableIntStateOf(0) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<CartItem?>(null) }

    LaunchedEffect(Unit) {
        if (token == null) {
            tokenViewModel.fetchToken()
        }
    }

    LaunchedEffect(token) {
        token?.let {
            cartViewModel.getCart("Bearer $it")
            checkoutViewModel.getCheckouts("Bearer $it")
        }
    }

    LaunchedEffect(checkoutSuccess) {
        if (checkoutSuccess) {
            token?.let {
                cartViewModel.getCart("Bearer $it")
                checkoutViewModel.resetCheckoutSuccess()
            }
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
                        val currentItemToDelete = itemToDelete
                        val currentToken = token

                        if (currentToken != null && currentItemToDelete != null) {
                            scope.launch {
                                try {
                                    cartViewModel.deleteCartItem(
                                        "Bearer $currentToken",
                                        currentItemToDelete.id
                                    )
                                } catch (e: Exception) {
                                    Log.e("TransactionScreen", "Error launching delete", e)
                                }
                            }
                        }
                        showDeleteDialog = false
                        itemToDelete = null
                    }
                ) {
                    Text("Remove")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        itemToDelete = null
                    }
                ) {
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
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Transaction",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
        TabRow(
            selectedTabIndex = showTab,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Tab(
                selected = showTab == 0,
                onClick = { showTab = 0 },
                text = { Text("Cart") }
            )
            Tab(
                selected = showTab == 1,
                onClick = { showTab = 1 },
                text = { Text("History") }
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                errorMessage != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = errorMessage!!)
                    }
                }
                else -> {
                    when (showTab) {
                        0 -> CartTab(
                            cartData = cartData,
                            selectedItems = selectedItems,
                            onSelectionChange = { selectedItems = it },
                            onDeleteItem = { item ->
                                itemToDelete = item
                                showDeleteDialog = true
                            },
                            onCheckout = { items ->
                                token?.let { token ->
                                    checkoutViewModel.checkout(
                                        "Bearer $token",
                                        items.toList()
                                    )
                                }
                            }
                        )
                        1 -> HistoryTab(checkoutViewModel = checkoutViewModel)
                    }
                }
            }
        }
    }
}

@Composable
private fun CartTab(
    cartData: CartData?,
    selectedItems: Set<Int>,
    onSelectionChange: (Set<Int>) -> Unit,
    onDeleteItem: (CartItem) -> Unit,
    onCheckout: (Set<Int>) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            cartData == null || cartData.cart.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Your cart is empty",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 80.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(cartData.cart) { cartItem ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Checkbox for selection
                                Checkbox(
                                    checked = selectedItems.contains(cartItem.id),
                                    onCheckedChange = { checked ->
                                        if (checked) {
                                            onSelectionChange(selectedItems + cartItem.id)
                                        } else {
                                            onSelectionChange(selectedItems - cartItem.id)
                                        }
                                    },
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )

                                // Product Image
                                if (cartItem.product.images.isNotEmpty()) {
                                    AsyncImage(
                                        model = cartItem.product.images.first(),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(80.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                    )
                                }

                                // Product Details
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

                                // Delete Button
                                IconButton(
                                    onClick = { onDeleteItem(cartItem) },
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
                }

                // Bottom Bar with Select All and Checkout Button
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(72.dp),
                    shadowElevation = 8.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Select All Checkbox
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Checkbox(
                                checked = selectedItems.size == cartData.cart.size,
                                onCheckedChange = { checked ->
                                    if (checked) {
                                        onSelectionChange(cartData.cart.map { it.id }.toSet())
                                    } else {
                                        onSelectionChange(emptySet())
                                    }
                                }
                            )
                            Text("Select All")
                        }

                        // Checkout Button
                        Button(
                            onClick = { onCheckout(selectedItems) },
                            enabled = selectedItems.isNotEmpty()
                        ) {
                            Text(
                                text = if (selectedItems.isEmpty()) {
                                    "Select items"
                                } else {
                                    "Checkout (${selectedItems.size})"
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryTab(
    checkoutViewModel: CheckoutViewModel
) {
    val checkouts by checkoutViewModel.checkoutData.collectAsState()
    val isLoading by checkoutViewModel.isLoading.collectAsState()

    when {
        isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        checkouts.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No checkout history")
            }
        }
        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(checkouts) { checkout ->
                    CheckoutHistoryCard(checkout)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}