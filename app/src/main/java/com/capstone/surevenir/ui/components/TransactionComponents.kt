package com.capstone.surevenir.ui.components


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.capstone.surevenir.data.network.response.CartItem
import com.capstone.surevenir.data.network.response.CheckoutData
import java.text.NumberFormat
import java.util.*

@Composable
fun CartItemCard(
    cartItem: CartItem,
    isSelected: Boolean,
    onSelectionChange: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = onSelectionChange
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = cartItem.product.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = formatPrice(cartItem.product.price),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${cartItem.quantity} items",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete"
                )
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Item") },
            text = { Text("Are you sure you want to remove this item from your cart?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun CheckoutHistoryCard(checkout: CheckoutData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Order #${checkout.id}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = checkout.status,
                    color = when (checkout.status) {
                        "COMPLETED" -> Color.Green
                        "PENDING" -> Color.Yellow
                        else -> Color.Red
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            checkout.checkoutDetails.forEach { detail ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${detail.productIdentity} x${detail.productQuantity}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = formatPrice(detail.productSubtotal.toInt()),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = formatPrice(checkout.totalPrice),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

private fun formatPrice(price: Int): String {
    return NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(price)
}