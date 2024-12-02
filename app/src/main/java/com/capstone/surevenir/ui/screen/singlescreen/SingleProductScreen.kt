package com.capstone.surevenir.ui.screen.singlescreen


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.capstone.surevenir.R

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.capstone.surevenir.data.network.response.ProductData
import com.capstone.surevenir.helper.formatPrice
import com.capstone.surevenir.ui.components.ProductCard
import com.capstone.surevenir.model.Product
import com.capstone.surevenir.model.Review
import com.capstone.surevenir.ui.screen.navmenu.ProductDetailSkeleton
import com.capstone.surevenir.ui.screen.navmenu.sfui_med
import com.capstone.surevenir.ui.screen.navmenu.sfui_semibold
import com.capstone.surevenir.ui.screen.navmenu.sfui_text
import com.capstone.surevenir.ui.viewmodel.MerchantDetailViewModel
import com.capstone.surevenir.ui.viewmodel.MerchantViewModel
import com.capstone.surevenir.ui.viewmodel.ProductDetailViewModel
import com.capstone.surevenir.ui.viewmodel.ProductViewModel
import com.capstone.surevenir.ui.viewmodel.TokenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@Composable
fun SingleProductScreen(
    productId: Int,
    navController: NavController,
    tokenViewModel: TokenViewModel = hiltViewModel(),
    viewModel: ProductDetailViewModel = hiltViewModel(),
    merchantDetailViewModel: MerchantDetailViewModel= hiltViewModel(),
    productViewModel: ProductViewModel = hiltViewModel()
) {
    val token by tokenViewModel.token.observeAsState()
    val productDetail by viewModel.productDetail.collectAsState()
    val error by viewModel.error.collectAsState()

    val merchantDetail by merchantDetailViewModel.merchantDetail.collectAsState()


    LaunchedEffect(Unit) {
        tokenViewModel.fetchToken()
    }

    LaunchedEffect(productId, token) {
        if (token != null) {
            Log.d("Debug", "Fetching product $productId")
            viewModel.fetchProductDetail(productId, token!!)

            delay(500) // Small delay to ensure product is fetched
            productDetail?.let { product ->
                Log.d("Debug", "Fetching merchant ${product.merchant_id}")
                merchantDetailViewModel.fetchMerchantDetail(product.merchant_id, token!!)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {  // Parent Box untuk layout
        if (productDetail != null && merchantDetail != null) {
            val product = productDetail!!
            val merchant = merchantDetail!!

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(bottom = 80.dp)  // Tambah padding untuk button
            ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE7E7E9))
                            .clickable { navController.popBackStack() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow),
                            contentDescription = "Back",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Text(
                        text = "Detail Souvenir",
                        fontSize = 25.sp,
                        fontFamily = sfui_semibold,
                        color = Color(0xFFCC5B14)
                    )
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    Spacer(modifier = Modifier.height(10.dp))
                    ImageSlider(images = product.images.map { it.url })
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        "Rp ${formatPrice(product.price)}",
                        fontSize = 24.sp,
                        fontFamily = sfui_semibold,
                        color = Color.Black
                    )
                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.Gray)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = product.name,
                        fontSize = 25.sp,
                        fontFamily = sfui_semibold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = product.description,
                        fontSize = 14.sp,
                        fontFamily = sfui_text,
                        color = Color.Gray
                    )
                }
            }

            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(15.dp)
                        .background(Color.LightGray)
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Details",
                        fontSize = 25.sp,
                        fontFamily = sfui_semibold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row {
                        Text(
                            text = "Category: ${product.product_categories}",
                            fontSize = 18.sp,
                            fontFamily = sfui_text,
                            color = Color.Gray
                        )
//                        Text(
//                            text = "Stock: ${product.stock}",
//                            fontSize = 18.sp,
//                            fontFamily = sfui_text,
//                            color = Color.Gray
//                        )
//                        Text(
//                            text = "Post at: ${product.createdAt}",
//                            fontSize = 18.sp,
//                            fontFamily = sfui_text,
//                            color = Color.Gray
//                        )
                    }

                }
            }

            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(15.dp)
                        .background(Color.LightGray)
                )
            }

            item {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = merchant.profileImageUrl ?: "https://via.placeholder.com/150",
                        contentDescription = merchant.name,
                        modifier = Modifier
                            .width(150.dp)
                            .clip(RoundedCornerShape(20.dp))
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = product.merchant.name,
                            fontFamily = sfui_semibold,
                            fontSize = 25.sp,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                text = "Click to see Shop Details",
                                fontFamily = sfui_med,
                                fontSize = 14.sp,
                                color = Color(0xFFFF7029)
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(15.dp)
                        .background(Color.LightGray)
                )
            }
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                val dummyReviews = listOf(
                    Review(
                        userName = "Abdul",
                        rating = 5,
                        date = "20 April 2024",
                        comment = "The Bali Hand Magnet captures Bali's vibrant culture through intricate hand gestures inspired by traditional dance."
                    ),
                    Review(
                        userName = "Abdul",
                        rating = 5,
                        date = "20 April 2024",
                        comment = "The Bali Hand Magnet captures Bali's vibrant culture through intricate hand gestures inspired by traditional dance."
                    )
                )

                ReviewsSection(
                    averageRating = 4.8,
                    totalReviews = 7,
                    reviews = dummyReviews
                )
            }
        }
            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(15.dp)
                        .background(Color.LightGray)
                )
            }

            item {
                Column(modifier = Modifier.fillMaxWidth()) {

                    Column (
                        modifier = Modifier
                            .padding(16.dp)
                    ){   Text(
                        text = "Other Products",
                        fontSize = 25.sp,
                        fontFamily = sfui_semibold,
                        color = Color.Black
                    ) }


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

                    ProductsSectionRandom(
                        products = products,
                        navController = navController
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

        }
        // In your Composable:
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.White)
                .shadow(elevation = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = { /* Handle add to cart */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF8B4513)  // Warna coklat sesuai gambar
//                )
            ) {
                Text(
                    text = "Add to Cart",
                    fontSize = 16.sp,
                    fontFamily = sfui_semibold,
                    color = Color.White
                )
            }
        }
    } else if (error != null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Error: ", color = Color.Red)
        }
    } else {
            ProductDetailSkeleton()
        }
    }
}

@Composable
fun ReviewsSection(
    averageRating: Double,
    totalReviews: Int,
    reviews: List<Review>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Reviews",
            fontSize = 25.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color(0xFFFFA726),
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "$averageRating",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Text(
                text = "/5.0",
                fontSize = 16.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = { }) {
                Text(
                    text = "$totalReviews review",
                    color = Color.Blue
                )
            }
        }

        reviews.forEach { review ->
            ReviewItem(review = review)
        }
        TextButton(
            onClick = { },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "See All Review",
                color = Color(0xFFFFA726),
                fontSize = 16.sp
            )

        }

    }

}


@Composable
fun ReviewItem(review: Review) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Surface(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                color = Color.LightGray
            ) {}

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = review.userName,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Row {
                    repeat(5) { index ->
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (index < review.rating) Color(0xFFFFA726) else Color.LightGray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = review.date,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Text(
            text = review.comment,
            modifier = Modifier.padding(top = 8.dp),
            fontSize = 14.sp
        )
    }

}

@Composable
fun ImageSlider(images: List<String>) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { images.size })
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Image(
                painter = rememberImagePainter(data = images[page]),
                contentDescription = "Product Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(images.size) { index ->
                Box(
                    modifier = Modifier
                        .size(if (pagerState.currentPage == index) 10.dp else 8.dp)
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(
                            if (pagerState.currentPage == index) Color.White else Color.Gray
                        )
                        .clickable {
                            coroutineScope.launch {
                                pagerState.scrollToPage(index)
                            }
                        }
                )
            }
        }
    }
}

@Composable
fun ProductsSectionRandom(products: MutableState<List<ProductData>?>, navController: NavController) {
    val randomProducts = remember(products.value) {
        products.value
            ?.filter { it.images.isNotEmpty() && it.name != null }
            ?.shuffled()  // Randomize the list
            ?.take(6)     // Take only 6 items
            ?: emptyList()
    }

    // Calculate the height needed for 3 rows (since we're showing 6 items in 2 columns)
    val gridHeight = 265.dp * 3 + 16.dp * 2 // (3 items * height) + (2 spacing gaps)

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.height(gridHeight) // Set fixed height
    ) {
        items(randomProducts) { product ->
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
