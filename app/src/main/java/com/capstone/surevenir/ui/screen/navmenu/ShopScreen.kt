package com.capstone.surevenir.ui.screen.navmenu

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.capstone.surevenir.BuildConfig
import com.capstone.surevenir.R
import com.capstone.surevenir.data.network.response.MerchantData
import com.capstone.surevenir.data.network.response.ProductData
import com.capstone.surevenir.ui.components.ProductCard
import com.capstone.surevenir.model.Category
import com.capstone.surevenir.model.Product
import com.capstone.surevenir.model.Merchant
import com.capstone.surevenir.ui.component.ShopCard
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
                    ShowSearchBar(onSearch = { query ->
                    }) {
                        scope.launch { bottomSheetState.show() }
                    }
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(8.dp))
                Text("Loading shop locations...")
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
                            .height(300.dp)
                            .padding(end = 10.dp)
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .clickable { navController.navigate("merchant/${shop.id}") }
                    )
                }
            }
        }
    }
}


@Composable
fun CategorySection(categories: MutableState<List<Category>?>, navController: NavHostController) {
    val categoryList = categories.value?.take(8) ?: emptyList()
    LazyRow (
    ){
        items(categoryList) { category ->
            Column (
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ){
                Text(
                    text = category.name,
                    fontFamily = sfui_semibold
                )
            }
        }
    }
}


@Composable
fun ShowSearchBar(onSearch: (String) -> Unit, onFilterClick: () -> Unit) {
    var query by remember { mutableStateOf("") }

    Column (
        modifier = Modifier.padding(1.dp)
    ){
        TextField(
            value = query,
            onValueChange = { query = it },
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
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch(query)
                }
            )
        )
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

