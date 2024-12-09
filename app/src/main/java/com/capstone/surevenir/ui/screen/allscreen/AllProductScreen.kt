package com.capstone.surevenir.ui.screen.allscreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.capstone.surevenir.R
import com.capstone.surevenir.ui.components.ProductCard
import com.capstone.surevenir.ui.screen.navmenu.sfui_semibold
import com.capstone.surevenir.ui.viewmodel.ProductViewModel
import com.capstone.surevenir.ui.viewmodel.TokenViewModel

@Composable
fun AllProductScreen(
    navController: NavHostController,
    tokenViewModel: TokenViewModel = hiltViewModel(),
    productViewModel: ProductViewModel = hiltViewModel()
) {
    val token by tokenViewModel.token.observeAsState()
    val pagingProducts = productViewModel.productPagingFlow.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        tokenViewModel.fetchToken()
    }

    LaunchedEffect(token) {
        token?.let { productViewModel.getProducts(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE7E7E9))
                    .clickable(onClick = { navController.popBackStack() }),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow),
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = "All Products",
                fontSize = 25.sp,
                fontFamily = sfui_semibold,
                color = Color(0xFFCC5B14)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                count = pagingProducts.itemCount,
                key = { index -> pagingProducts[index]?.id ?: index }
            ) { index ->
                pagingProducts[index]?.let { product ->
                    ProductCard(
                        product = product,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(265.dp)
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
                            .clickable { navController.navigate("product/${product.id}") }
                    )
                }
            }
        }
    }
}