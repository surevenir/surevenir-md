package com.capstone.surevenir.ui.screen.navmenu

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.capstone.surevenir.data.network.response.CartData
import com.capstone.surevenir.data.network.response.CartItem
import com.capstone.surevenir.ui.components.CartEmptyState
import com.capstone.surevenir.ui.components.CartItemCard
import com.capstone.surevenir.ui.components.CheckoutHistoryCard
import com.capstone.surevenir.ui.components.CustomTabLayout
import com.capstone.surevenir.ui.viewmodel.CartViewModel
import com.capstone.surevenir.ui.viewmodel.CheckoutViewModel
import com.capstone.surevenir.ui.viewmodel.TokenViewModel
import kotlinx.coroutines.launch

@Composable
fun TransactionScreen(
    navController: NavHostController,
    cartViewModel: CartViewModel = hiltViewModel(),
    checkoutViewModel: CheckoutViewModel = hiltViewModel(),
    tokenViewModel: TokenViewModel = hiltViewModel(),
) {
    val cartData by cartViewModel.cartData.collectAsState()
    val isLoading by cartViewModel.isLoading.collectAsState()
    val errorMessage by cartViewModel.errorMessage.collectAsState()
    val token by tokenViewModel.token.observeAsState()
    val checkoutSuccess by checkoutViewModel.checkoutSuccess.collectAsState()
    val scope = rememberCoroutineScope()

    var selectedItems by remember { mutableStateOf(setOf<Int>()) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<CartItem?>(null) }

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    var showTab by remember {
        mutableIntStateOf(savedStateHandle?.get<Int>("selectedTab") ?: 0)
    }

    LaunchedEffect(Unit) {
        if (token == null) {
            tokenViewModel.fetchToken()
        }
        savedStateHandle?.remove<Int>("selectedTab")
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
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 10.dp, bottom = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Transaction",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
        CustomTabLayout(
            selectedTab = showTab,
            onTabSelected = { showTab = it }
        )
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
                            navController = navController,
                            cartData = cartData,
                            selectedItems = selectedItems,
                            onSelectionChange = { selectedItems = it },
                            onDeleteItems = { itemsToDelete ->
                                itemsToDelete.forEach { id ->
                                    val itemToRemove = cartData?.cart?.find { it.id == id }
                                    itemToRemove?.let {
                                        itemToDelete = it
                                        showDeleteDialog = true
                                    }
                                }
                            },
                            onUpdateQuantity = { cartItem, newQuantity ->
                                token?.let { token ->
                                    scope.launch {
                                        try {
                                            cartViewModel.updateCartItemQuantity(
                                                "Bearer $token",
                                                cartItem.id,
                                                newQuantity
                                            )
                                        } catch (e: Exception) {
                                            Log.e("TransactionScreen", "Error updating quantity", e)
                                        }
                                    }
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
    navController: NavController,
    cartData: CartData?,
    selectedItems: Set<Int>,
    onSelectionChange: (Set<Int>) -> Unit,
    onDeleteItems: (Set<Int>) -> Unit,
    onUpdateQuantity: (CartItem, Int) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            cartData == null || cartData.cart.isEmpty() -> {
                CartEmptyState(navController = navController)
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp)
                ) {
                    // Selection header
                    if (selectedItems.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${selectedItems.size} product selected",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            TextButton(
                                onClick = { onDeleteItems(selectedItems) }
                            ) {
                                Text(
                                    text = "Delete",
                                    color = Color.Red
                                )
                            }
                        }
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(cartData.cart) { cartItem ->
                            CartItemCard(
                                cartItem = cartItem,
                                isSelected = selectedItems.contains(cartItem.id),
                                onSelectionChange = { checked ->
                                    if (checked) {
                                        onSelectionChange(selectedItems + cartItem.id)
                                    } else {
                                        onSelectionChange(selectedItems - cartItem.id)
                                    }
                                },
                                onQuantityChange = { newQuantity ->
                                    onUpdateQuantity(cartItem, newQuantity)
                                },
                                onProductClick = { productId ->
                                    navController.navigate("product/$productId")
                                }
                            )
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

                        Button(
                            onClick = {
                                val selectedCartItems = cartData.cart.filter {
                                    selectedItems.contains(it.id)
                                }
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    "selectedItems",
                                    selectedCartItems
                                )
                                navController.navigate("checkout")
                            },
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

    val sortedCheckouts = remember(checkouts) {
        checkouts.sortedByDescending { it.createdAt }
    }

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
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = sortedCheckouts,
                    key = { it.id }
                ) { checkout ->
                    CheckoutHistoryCard(checkout = checkout)
                }
            }
        }
    }
}