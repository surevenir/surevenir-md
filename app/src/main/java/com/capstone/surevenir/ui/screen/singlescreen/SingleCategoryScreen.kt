package com.capstone.surevenir.ui.screen.singlescreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.capstone.surevenir.R
import com.capstone.surevenir.model.Product
import com.capstone.surevenir.ui.components.ProductCard
import com.capstone.surevenir.ui.screen.navmenu.sfui_med
import com.capstone.surevenir.ui.screen.navmenu.sfui_semibold
import com.capstone.surevenir.ui.screen.navmenu.sfui_text
import com.capstone.surevenir.ui.viewmodel.CategoryViewModel
import com.capstone.surevenir.ui.viewmodel.ProductDetailViewModel
import com.capstone.surevenir.ui.viewmodel.ProductViewModel
import com.capstone.surevenir.ui.viewmodel.TokenViewModel

@Composable
fun SingleCategoryScreen (
    categoryId: Int,
    navController: NavHostController,
    tokenViewModel: TokenViewModel = hiltViewModel(),
    viewModel: CategoryViewModel = hiltViewModel(),
    productViewModel: ProductViewModel = hiltViewModel(),
    ){
    val categoryProducts by productViewModel.categoryProducts.collectAsState()

    val token by tokenViewModel.token.observeAsState()
    val productDetail by viewModel.categoryDetail.collectAsState()
    val error by viewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        tokenViewModel.fetchToken()
    }

    LaunchedEffect(categoryId, token) {
        if (token != null) {
            Log.d("SingleProductScreen Debug", "Token is available: $token")
            viewModel.getCategoryDetail(categoryId, token!!)
        } else {
            Log.d("SingleProductScreen Debug", "Token is still null")
        }
    }

    if (productDetail != null) {
        val product = productDetail!!
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    )
    {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(start = 5.dp, bottom = 0.dp),
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
                    text = "Detail Category",
                    fontSize = 25.sp,
                    fontFamily = sfui_semibold,
                    color = Color(0xFFCC5B14)
                )
            }
            Spacer(
                modifier = Modifier
                    .padding(top = 15.dp)
                    .fillMaxWidth()
                    .height(0.4.dp)
                    .background(Color.Gray)
            )
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.cat_art),
                        contentDescription = "Art",
                        modifier = Modifier
                            .size(150.dp)
                            .padding(5.dp)
                            .clickable { navController.navigate("singleCategory") }
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Text(
                        text = "${product.name}",
                        fontSize = 35.sp,
                        fontFamily = sfui_semibold
                    )
                    Spacer(modifier = Modifier
                        .height(15.dp))
                    Text(
                        text = "${product.description}",
                        fontSize = 20.sp,
                        fontFamily = sfui_text
                    )
                    Spacer(modifier = Modifier
                        .height(15.dp)
                    )
                    Text(
                        text = "Rp ${product.rangePrice}",
                        fontSize = 20.sp,
                        fontFamily = sfui_semibold
                    )
                }
            }
        }

        item {
            CategoryProductsSection(
                categoryId = categoryId,
                productViewModel = productViewModel,
                navController = navController
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
        }
    }
}

@Composable
fun CategoryProductsSection(
    categoryId: Int,
    productViewModel: ProductViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val categoryProducts by productViewModel.categoryProducts.collectAsState()

    LaunchedEffect(categoryId) {
        productViewModel.getProductsByCategoryId(categoryId)
    }

    Column(modifier = modifier) {
        Text(
            text = "Products",
            fontFamily = sfui_semibold,
            fontSize = 20.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        if (categoryProducts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No products exist yet from this shop",
                    fontFamily = sfui_med,
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            categoryProducts.chunked(2).forEach { rowProducts ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    rowProducts.forEach { product ->
                        ProductCard(
                            product = product,
                            modifier = Modifier
                                .weight(1f)
                                .height(265.dp)
                                .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
                                .clickable {
                                    navController.navigate("product/${product.id}")
                                }
                        )
                    }
                    if (rowProducts.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}