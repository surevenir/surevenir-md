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
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.Checkbox
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.RadioButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
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
    val pagingProducts = productViewModel.productPagingFlow.collectAsLazyPagingItems()


    val token by tokenViewModel.token.observeAsState()

    LaunchedEffect(Unit) {
        tokenViewModel.fetchToken()
    }

    LaunchedEffect(token) {
        token?.let { productViewModel.getProducts(it) }
    }

    LaunchedEffect(productViewModel.minPriceFilter, productViewModel.maxPriceFilter, productViewModel.minStockFilter, productViewModel.startDateFilter, productViewModel.endDateFilter) {
        // Jika semua filter bernilai null, mungkin tidak perlu apply
        // Tapi jika salah satu berubah atau user baru saja set filter, panggil applyFilter
        if (productViewModel.minPriceFilter != null || productViewModel.maxPriceFilter != null || productViewModel.minStockFilter != null || productViewModel.startDateFilter != null || productViewModel.endDateFilter != null) {
            productViewModel.applyFilter()
        }
    }

    Log.d("TOKEN_CATE", token.toString())

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            FilterBottomSheet(
                onFilterApplied = {
                    scope.launch {
                        bottomSheetState.hide()
                    }
                }
            )
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
                            merchantViewModel.getMerchants(token!!) { merchantList ->  // Hapus "Bearer " karena sudah ditangani di ViewModel
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
                    HomeProductsSection(navController, pagingProducts)
                    Spacer(modifier = Modifier.height(16.dp))
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
            val latitude = shop.latitude?.toDoubleOrNull()
            val longitude = shop.longitude?.toDoubleOrNull()

            if (latitude != null && longitude != null && latitude in -90.0..90.0 && longitude in -180.0..180.0) {
                geocodingViewModel.getSubDistrictFromCoordinates(longitude, latitude, apiKey) { subDistrict ->
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
                        totalShopProduct = shop.product_count,
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
                                    ShopData(shop.location ?: "No Location", shop.product_count)
                                )
                                Log.d(
                                    "Navigation",
                                    "Location: ${shop.location}, Count: ${shop.product_count}"
                                )
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
                modifier = Modifier.padding(10.dp)
                    .clickable { navController.navigate("category/${category.id}") },
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
fun FilterBottomSheet(
    viewModel: ProductViewModel = hiltViewModel(),
    onFilterApplied: () -> Unit = {}
) {
    var minPrice by remember { mutableStateOf("") }
    var maxPrice by remember { mutableStateOf("") }
    var minStock by remember { mutableStateOf("") }

    var startDate by remember { mutableStateOf("") } // date dalam string, nanti convert ke long
    var endDate by remember { mutableStateOf("") }

    var selectedPriceOption by remember { mutableStateOf<String?>(null) }
    var selectedStockOption by remember { mutableStateOf<String?>(null) }
    var isNewRelease by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White)
    ) {
        Text(
            text = "Filter",
            fontFamily = sfui_semibold,
            color = Color(0xFFCC5B14),
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            "Filter Price",
            fontFamily = sfui_med
        )
        Spacer(modifier = Modifier.height(8.dp))
        PriceOption(
            label = "Under 100.000",
            selected = selectedPriceOption == "under100",
            onSelect = { selectedPriceOption = "under100" }
        )
        PriceOption(
            label = "100.000 - 300.000",
            selected = selectedPriceOption == "range100to300",
            onSelect = { selectedPriceOption = "range100to300" }
        )
        PriceOption(
            label = "Over 300.000",
            selected = selectedPriceOption == "over300",
            onSelect = { selectedPriceOption = "over300" }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Filter Stock",
            fontFamily = sfui_med
        )

        Spacer(modifier = Modifier.height(8.dp))
        PriceOption(
            label = "Under 5",
            selected = selectedStockOption == "under5",
            onSelect = { selectedStockOption = "under5" },
        )
        PriceOption(
            label = "5 - 30",
            selected = selectedStockOption == "5to30",
            onSelect = { selectedStockOption = "5to30" }
        )
        PriceOption(
            label = "30 - 100",
            selected = selectedStockOption == "30to100",
            onSelect = { selectedStockOption = "30to100" }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Filter by Date",
            fontFamily = sfui_med
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isNewRelease,
                onCheckedChange = { isNewRelease = it }
            )
            Text(
                text = "New Release (<7 recent days)",
                fontFamily = sfui_text,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFED8A00)),
            onClick = {
                val (minPriceVal, maxPriceVal) = when(selectedPriceOption) {
                    "under100" -> Pair(null, 100000.0)
                    "range100to300" -> Pair(100000.0, 300000.0)
                    "over300" -> Pair(300000.0, null)
                    else -> Pair(null, null)
                }

                val (minStockVal, maxStockVal) = when(selectedStockOption) {
                    "under5" -> Pair(null, 5)
                    "5to30" -> Pair(5, 30)
                    "30to100" -> Pair(30, 100)
                    else -> Pair(null, null)
                }
                val now = System.currentTimeMillis()
                val sevenDaysAgo = now - 7 * 24 * 60 * 60 * 1000
                val startDateVal = if (isNewRelease) sevenDaysAgo else null
                val endDateVal = if (isNewRelease) now else null

                viewModel.minPriceFilter = minPriceVal
                viewModel.maxPriceFilter = maxPriceVal
                viewModel.minStockFilter = minStockVal

                viewModel.startDateFilter = startDateVal
                viewModel.endDateFilter = endDateVal

                onFilterApplied()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Apply Filter",
                fontFamily = sfui_semibold,
            )
        }
    }
}

@Composable
fun PriceOption(label: String, selected: Boolean, onSelect: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onSelect() }
            .padding(vertical = 4.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect
        )
        Text(
            text = label,
            modifier = Modifier.padding(start = 8.dp),
            fontFamily = sfui_text,
        )
    }
}

fun parseDateToLong(dateString: String): Long? {
    return try {
        val formatter = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        val date = formatter.parse(dateString)
        date?.time
    } catch (e: Exception) {
        null
    }
}


