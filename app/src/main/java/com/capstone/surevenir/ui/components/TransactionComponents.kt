package com.capstone.surevenir.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.capstone.surevenir.R
import com.capstone.surevenir.data.network.response.CartItem
import com.capstone.surevenir.data.network.response.CheckoutData
import com.capstone.surevenir.helper.DateConverter
import com.capstone.surevenir.helper.formatCurrency
import com.capstone.surevenir.ui.screen.navmenu.sfui_med
import com.capstone.surevenir.ui.screen.navmenu.sfui_semibold


@Composable
fun CustomTabLayout(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CustomTab(
            text = "Chart",
            isSelected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            modifier = Modifier.weight(1f)
        )
        CustomTab(
            text = "Checkout",
            isSelected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun CustomTab(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) {
        Color(0xFFED8A00)
    } else {
        Color(0xFFE0E0E0)
    }

    val textColor = if (isSelected) {
        Color.White
    } else {
        Color.Gray
    }

    Button(
        onClick = onClick,
        modifier = modifier
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text,
            fontFamily = sfui_semibold,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun CartItemCard(
    cartItem: CartItem,
    isSelected: Boolean,
    onSelectionChange: (Boolean) -> Unit,
    onQuantityChange: (Int) -> Unit,
    onProductClick: (Int) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onProductClick(cartItem.product.id) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (cartItem.product.images.isNotEmpty()) {
                AsyncImage(
                    model = cartItem.product.images.first(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { onProductClick(cartItem.product.id) }
                )
            }

            // Product Details
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 20.dp, end = 5.dp, top = 20.dp, bottom = 20.dp)
            ) {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_shop),
                        contentDescription = null,
                        modifier = Modifier.size(10.dp),
                        colorFilter = ColorFilter.tint(Color.Gray)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = cartItem.product.merchant.name,
                        fontFamily = sfui_med,
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = cartItem.product.name,
                        fontWeight = FontWeight.Bold,
                        fontFamily = sfui_semibold,
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Checkbox(
                        checked = isSelected,
                        onCheckedChange = onSelectionChange,
                        modifier = Modifier.padding(start = 10.dp),
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFFED8A00),
                            uncheckedColor = Color.Gray,
                            checkmarkColor = Color.White
                        )
                    )
                }

                Text(
                    text = formatCurrency(cartItem.product.price),
                    color = Color.Gray,
                    fontSize = 12.sp,
                    fontFamily = sfui_med
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${cartItem.quantity} items",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontFamily = sfui_med
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Sub total:",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontFamily = sfui_med
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatCurrency(cartItem.subtotalPrice),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontFamily = sfui_semibold
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                if (cartItem.quantity > 1) {
                                    onQuantityChange(cartItem.quantity - 1)
                                }
                            },
                            modifier = Modifier.size(24.dp),
                            enabled = cartItem.quantity > 1
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Decrease quantity"
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = {
                                if (cartItem.quantity < cartItem.product.stock) {
                                    onQuantityChange(cartItem.quantity + 1)
                                }
                            },
                            modifier = Modifier.size(24.dp),
                            enabled = cartItem.quantity < cartItem.product.stock
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Increase quantity",
                                tint = if (cartItem.quantity < cartItem.product.stock)
                                    LocalContentColor.current
                                else
                                    Color.Gray.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CheckoutHistoryCard(
    checkout: CheckoutData,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val dateConverter = DateConverter()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            // Date and Status Row
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ORDER",
                    fontFamily = sfui_semibold,
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.ExtraBold

                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = dateConverter.formatDateString(checkout.createdAt.substring(0, 10)),
                    fontSize = 12.sp,
                    fontFamily = sfui_semibold,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = checkout.status,
                    fontSize = 12.sp,
                    fontFamily = sfui_semibold,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(
                            when (checkout.status) {
                                "COMPLETED" -> Color(0xFF86EFAC)
                                "PENDING" -> Color(0xFFFDE047)
                                else -> Color(0xFFFCA5A5)
                            },
                            RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    color = when (checkout.status) {
                        "COMPLETED" -> Color(0xFF166534)
                        "PENDING" -> Color(0xFF854D0E)
                        else -> Color(0xFF991B1B)
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            checkout.checkoutDetails.firstOrNull()?.let { detail ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = detail.product.name,
                        fontFamily = sfui_semibold,
                        fontSize = 14.sp,
                        maxLines = 1,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = formatCurrency(detail.productSubtotal.toInt()),
                            fontFamily = sfui_semibold,
                            fontSize = 14.sp,
                            maxLines = 1,
                            color = Color.Black
                        )
                        Text(
                            text = "x${detail.productQuantity}",
                            fontFamily = sfui_semibold,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_shop),
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            colorFilter = ColorFilter.tint(Color.Gray)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = detail.product.merchant.name,
                            fontFamily = sfui_med,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            // Additional Products Count
            if (checkout.checkoutDetails.size > 1) {
                Text(
                    text = "+${checkout.checkoutDetails.size - 1} Other Products",
                    fontFamily = sfui_semibold,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp))

            // Grand Total
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Grand total:",
                    fontFamily = sfui_semibold,
                    fontSize = 12.sp,
                    color = Color.Gray,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = formatCurrency(checkout.totalPrice),
                    fontFamily = sfui_semibold,
                    fontSize = 14.sp,
                    color = Color.Black,
                )
                Spacer(modifier = Modifier.weight(1f))
                if (checkout.status == "COMPLETED") {
                    Button(
                        onClick = onClick,
                        modifier = Modifier
                            .width(100.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xFFED8A00))
                    ) {
                        Text(
                            text = "Rate",
                            fontFamily = sfui_semibold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyStateMessage(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String,
    onEmptyStateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .clickable { onEmptyStateClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        icon()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            fontSize = 18.sp,
            color = Color.Gray,
            fontFamily = sfui_semibold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = subtitle,
            fontSize = 12.sp,
            fontFamily = sfui_med,
            color = Color.Gray.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CartEmptyState(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    EmptyStateMessage(
        icon = {
            Icon(
                imageVector = Icons.Filled.ShoppingCart,
                contentDescription = "Navigate to Shop",
                modifier = Modifier
                    .size(70.dp)
                    .background(
                        color = Color.LightGray.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(16.dp),
                tint = Color.Gray
            )
        },
        title = "Looks empty here!",
        subtitle = "Time to explore great deals",
        onEmptyStateClick = {
            navController.navigate("shop")
        },
        modifier = modifier
    )
}