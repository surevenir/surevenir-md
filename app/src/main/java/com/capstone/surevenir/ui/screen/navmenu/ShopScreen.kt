package com.capstone.surevenir.ui.screen.navmenu

import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.capstone.surevenir.BuildConfig
import com.capstone.surevenir.R
import com.capstone.surevenir.data.network.response.MerchantData
import com.capstone.surevenir.data.network.response.ProductData
import com.capstone.surevenir.model.Category
import com.capstone.surevenir.model.ShopData
import com.capstone.surevenir.ui.components.ProductCard
import com.capstone.surevenir.ui.components.ShopCard
import com.capstone.surevenir.ui.components.SectionHeader
import com.capstone.surevenir.ui.viewmodel.CategoryViewModel
import com.capstone.surevenir.ui.viewmodel.GeocodingViewModel
import com.capstone.surevenir.ui.viewmodel.MerchantViewModel
import com.capstone.surevenir.ui.viewmodel.ProductViewModel
import com.capstone.surevenir.ui.viewmodel.TokenViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShopScreen(navController: NavHostController, tokenViewModel: TokenViewModel = hiltViewModel(), categoryViewModel: CategoryViewModel = hiltViewModel(), merchantViewModel: MerchantViewModel = hiltViewModel(), productViewModel: ProductViewModel = hiltViewModel()) {
    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val merchants = remember { mutableStateOf<List<MerchantData>?>(null) }
    val categoryList = remember { mutableStateOf<List<Category>?>(null) }
    val productList = remember { mutableStateOf<List<ProductData>?>(null) }


    val token by tokenViewModel.token.observeAsState()

    LaunchedEffect(Unit) {
        tokenViewModel.fetchToken()
    }

    Log.d("TOKEN_CATE", token.toString())

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            FilterBottomSheet()
        },
        scrimColor = Color.Black.copy(alpha = 0.5f)
    ) {
        Scaffold { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(10.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item {
                    ShowSearchBar(
                        viewModel = productViewModel,
                        onSearch = { query ->
                            productViewModel.searchProducts(query)
                        },
                        onFilterClick = {
                            scope.launch {
                                bottomSheetState.show()
                            }
                        },
                        navController = navController
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    SectionHeader(title = "Categories", actionText = "All Categories", navController)
                }
                item {
                    if (token != null) {
                        LaunchedEffect(token) {
                            Log.d("TOKEN_CATE", "Using Token: $token")
                            categoryViewModel.getCategories("Bearer $token") { categories ->
                                categoryList.value = categories
                            }
                        }
                    } else {
                        Log.d("TOKEN_CATE", "Token belum tersedia")
                    }
                    CategorySection(categories = categoryList, navController)
                    Spacer(modifier = Modifier.height(10.dp))

                }

                item {
                    SectionHeader(title = "Shops", actionText = "All Shops", navController)
                    Spacer(modifier = Modifier.height(10.dp))

                }
                item {
                    if (token != null) {
                        LaunchedEffect(token) {
                            merchantViewModel.getMerchants("Bearer $token") { merchantList ->
                                merchants.value = merchantList
                            }
                        }
                    }
                    ShopSection(shops = merchants.value ?: emptyList(), navController)
                    Spacer(modifier = Modifier.height(16.dp))

                }

                item {
                    SectionHeader(title = "All Products", actionText = "All Products", navController)
                }
                item {
                    LaunchedEffect(token) {
                        if (token != null) {
                            Log.d("TOKEN_CATE", "Using Token: $token")
                            productViewModel.getProducts("Bearer $token")
                        } else {
                            Log.d("TOKEN_CATE", "Token belum tersedia")
                            productViewModel.getAllProducts()
                        }
                    }
                    val products = remember { mutableStateOf<List<ProductData>?>(null) }
                    products.value = productViewModel.products.collectAsState().value
                    ProductsSection(products = products, navController)
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
            }
        }
    }


@Composable
fun ShopSection(
    shops: List<MerchantData>,
    navController: NavHostController,
    geocodingViewModel: GeocodingViewModel = hiltViewModel()
) {
    val updatedShops = remember(shops) { mutableStateOf(shops) }
    val isLoading = remember { mutableStateOf(true) }
    val apiKey = BuildConfig.MAPS_API_KEY
    val remainingRequests = remember { mutableStateOf(shops.size) }

    Log.d("ShopSection", "Initial shops size: ${shops.size}")

    LaunchedEffect(shops) {
        Log.d("ShopSection", "LaunchedEffect triggered. Shops size: ${shops.size}")
        isLoading.value = true

        if (shops.isEmpty()) {
            Log.d("ShopSection", "No shops to process, setting loading to false")
            isLoading.value = false
            return@LaunchedEffect
        }

        shops.forEach { shop ->
            Log.d("ShopValidation", "Processing shop - ID: ${shop.id}, Latitude: ${shop.latitude}, Longitude: ${shop.longitude}")
            val latitude = shop.longitude?.toDoubleOrNull()
            val longitude = shop.latitude?.toDoubleOrNull()

            if (latitude != null && longitude != null && latitude in -90.0..90.0 && longitude in -180.0..180.0) {
                geocodingViewModel.getSubDistrictFromCoordinates(latitude, longitude, apiKey) { subDistrict ->
                    Log.d("GeocodingResult", "Received subDistrict for Shop ID ${shop.id}: $subDistrict")

                    val currentShops = updatedShops.value.toMutableList()
                    val updatedIndex = currentShops.indexOfFirst { it.id == shop.id }

                    if (updatedIndex != -1) {
                        val updatedShop = currentShops[updatedIndex].copy(
                            location = subDistrict ?: "No Location"
                        )
                        currentShops[updatedIndex] = updatedShop
                        updatedShops.value = currentShops

                        Log.d("ShopSection", "Updated shop ${shop.id} location to: ${subDistrict}")
                    }

                    remainingRequests.value -= 1
                    Log.d("ShopSection", "Remaining requests: ${remainingRequests.value}")

                    if (remainingRequests.value <= 0) {
                        Log.d("ShopSection", "All requests completed, setting loading to false")
                        isLoading.value = false
                    }
                }
            } else {
                Log.e("ShopSection", "Invalid coordinates for shop ${shop.id}")
                remainingRequests.value -= 1
                if (remainingRequests.value <= 0) {
                    isLoading.value = false
                }
            }
        }
    }

    if (isLoading.value) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(5) {
                ShopCardSkeleton()
            }
        }
    } else {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(
                items = updatedShops.value.take(5),
                key = { it.id }
            ) { shop ->
                shop.profile_image_url?.let {
                    Log.d("ShopSection", "Rendering shop card - ID: ${shop.id}, Name: ${shop.name}, Location: ${shop.location}")
                    ShopCard(
                        imageRes = it,
                        shopName = shop.name,
                        shopLocation = shop.location ?: "No Location",
                        totalShopProduct = shop.products_count,
                        modifier = Modifier
                            .width(200.dp)
                            .height(350.dp)
                            .padding(end = 10.dp)
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .clickable {
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    "shopData",
                                    ShopData(shop.location ?: "No Location", shop.products_count)
                                )
                                Log.d("Navigation", "Location: ${shop.location}, Count: ${shop.products_count}")
                                navController.navigate("merchant/${shop.id}")
                            }
                    )
                }
            }
        }
    }
}

@Composable
fun ShimmerBrush(showShimmer: Boolean = true, targetValue: Float = 1000f): Brush {
    return if (showShimmer) {
        val shimmerColors = listOf(
            Color.LightGray.copy(alpha = 0.6f),
            Color.LightGray.copy(alpha = 0.2f),
            Color.LightGray.copy(alpha = 0.6f),
        )

        val transition = rememberInfiniteTransition(label = "")
        val translateAnimation = transition.animateFloat(
            initialValue = 0f,
            targetValue = targetValue,
            animationSpec = infiniteRepeatable(
                animation = tween(800, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = ""
        )

        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset.Zero,
            end = Offset(x = translateAnimation.value, y = translateAnimation.value)
        )
    } else {
        Brush.linearGradient(
            colors = listOf(Color.Transparent, Color.Transparent),
            start = Offset.Zero,
            end = Offset.Zero
        )
    }
}

@Composable
fun ShopCardSkeleton(
    modifier: Modifier = Modifier
) {
    val shimmerBrush = ShimmerBrush()

    Column(
        modifier = modifier
            .width(200.dp)
            .height(300.dp)
            .padding(end = 10.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(brush = shimmerBrush)
        )

        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(20.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(brush = shimmerBrush)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(brush = shimmerBrush)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(brush = shimmerBrush)
            )
        }
    }
}



@Composable
fun CategorySection(categories: MutableState<List<Category>?>, navController: NavHostController) {
    val categoryList = categories.value?.take(8) ?: emptyList()
    LazyRow {
        items(categoryList) { category ->
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = category.image_url ?: "https://via.placeholder.com/150",
                    contentDescription = category.name,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = category.name,
                    fontFamily = sfui_semibold
                )
            }
        }
    }
}


// ShowSearchBar.kt
@Composable
fun ShowSearchBar(
    onSearch: (String) -> Unit,
    onFilterClick: () -> Unit,
    viewModel: ProductViewModel = hiltViewModel(),
    navController: NavHostController
) {
    var query by remember { mutableStateOf("") }
    val searchResults by viewModel.searchResults.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        TextField(
            value = query,
            onValueChange = {
                query = it
                viewModel.searchProducts(it)
            },
            label = { Text("Search Souvenir") },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.icon_filter),
                    contentDescription = "Filter Icon",
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable {
                            onFilterClick()
                        }
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFE9E9EC),
                unfocusedContainerColor = Color(0xFFE9E9EC),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Black,
                focusedLabelColor = Color.Gray,
                unfocusedLabelColor = Color.LightGray
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch(query) })
        )

        if (query.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))

            if (searchResults.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No products found")
                }
            } else {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(searchResults) { product ->
                        ProductCard(
                            product = product,
                            modifier = Modifier
                                .width(160.dp)
                                .padding(end = 8.dp)
                                .clickable {
                                    navController.navigate("product/${product.id}")
                                }
                        )
                    }
                }
            }
        }
    }
}



@Composable
fun FilterBottomSheet() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White)
    ) {
        Text(
            text = "Filter",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text("Filter Kategori")
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { /* Tambahkan logika filter */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Terapkan Filter")
        }
    }
}

