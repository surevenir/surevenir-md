package com.capstone.surevenir.ui.screen.allscreen

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.capstone.surevenir.R
import com.capstone.surevenir.data.network.response.ProductData
import com.capstone.surevenir.ui.components.ProductCard
import com.capstone.surevenir.model.Product
import com.capstone.surevenir.ui.screen.navmenu.ProductsSection
import com.capstone.surevenir.ui.screen.navmenu.sfui_semibold
import com.capstone.surevenir.ui.viewmodel.ProductViewModel
import com.capstone.surevenir.ui.viewmodel.TokenViewModel

@Composable
fun AllProductScreen(navController: NavHostController,  tokenViewModel: TokenViewModel = hiltViewModel(), productViewModel: ProductViewModel = hiltViewModel()) {
    val productList = remember { mutableStateOf<List<ProductData>?>(null) }


    val token by tokenViewModel.token.observeAsState()

    LaunchedEffect(Unit) {
        tokenViewModel.fetchToken()
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

        if (token != null) {
            LaunchedEffect(token) {
                if (token != null) {
                    Log.d("TOKEN_CATE", "Using Token: $token")
                    productViewModel.getProducts("Bearer $token")
                } else {
                    Log.d("TOKEN_CATE", "Token belum tersedia")
                    productViewModel.getAllProducts()
                }
            }
        } else {
            Log.d("TOKEN_CATE", "Token belum tersedia")
        }
        val products = remember { mutableStateOf<List<ProductData>?>(null) }
        products.value = productViewModel.products.collectAsState().value
        ProductsSectionAll(products = products, navController)
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun ProductsSectionAll(products: MutableState<List<ProductData>?>, navController: NavController) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        products.value?.let { productList ->
            items(productList.filter { it.images.isNotEmpty() && it.name != null }) { product ->
                ProductCard(
                    product = product,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(265.dp)
                        .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
                        .clickable {
                            navController.navigate("product/${product.id}")
                        }
                )
            }
        }
    }
}

