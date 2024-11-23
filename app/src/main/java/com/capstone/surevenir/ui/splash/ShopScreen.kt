package com.capstone.surevenir.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.capstone.surevenir.R
import com.capstone.surevenir.components.ProductCard
import com.capstone.surevenir.model.Category
import com.capstone.surevenir.model.Product
import com.capstone.surevenir.model.Shop
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShopScreen(navController: NavHostController) {
    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

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
                    val categoryList = listOf(
                        Category(R.drawable.cat_art, "Art"),
                        Category(R.drawable.cat_furniture, "Furniture"),
                        Category(R.drawable.cat_furniture, "Furniture"),
                        Category(R.drawable.cat_furniture, "Furniture"),
                        Category(R.drawable.cat_toy, "Toy"),
                        Category(R.drawable.cat_toy, "Toy"),
                        Category(R.drawable.cat_toy, "Toy"),
                        Category(R.drawable.cat_spice, "Spice")
                    )
                    CategorySection(categories = categoryList)
                    Spacer(modifier = Modifier.height(10.dp))

                }

                item {
                    SectionHeader(title = "Shops", actionText = "All Shops", navController)
                    Spacer(modifier = Modifier.height(10.dp))

                }
                item {
                    val shopList = listOf(
                        Shop(R.drawable.shop, "Ketut Art", "Ubud", 10),
                        Shop(R.drawable.shop, "Ketut Art", "Ubud", 10),
                        Shop(R.drawable.shop, "Ketut Art", "Ubud", 10)
                    )
                    ShopSection(shops = shopList)
                    Spacer(modifier = Modifier.height(16.dp))

                }

                item {
                    SectionHeader(title = "Popular Products", actionText = "All Products", navController)
                }
                item {
                    val popularProducts = listOf(
                        Product(
                            R.drawable.product_image,
                            "Bali Hand Magnet",
                            "Magnet souvenir for your fridge.",
                            "IDR 25.000"
                        ),
                        Product(
                            R.drawable.product_image,
                            "Keychain",
                            "Customizable keychains for your loved ones.",
                            "IDR 15.000"
                        ),
                        Product(
                            R.drawable.product_image,
                            "T-shirt Bali",
                            "High-quality Bali-themed T-shirt.",
                            "IDR 150.000"
                        )
                    )
                    PopolarProductSection(popularProduct = popularProducts)
                }
            }
        }
    }
}



@Composable
fun SectionHeader(title: String, actionText: String, navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        androidx.compose.material.Text(
            text = title,
            fontSize = 20.sp,
            fontFamily = sfui_semibold
        )
        androidx.compose.material.Text(
            text = actionText,
            fontSize = 20.sp,
            fontFamily = sfui_semibold,
            color = Color(0xFFCC5B14),
            modifier = Modifier
                .clickable {
                    if (actionText == "All Shops"){
                        navController.navigate("allShop")
                    }
                    else if (actionText == "All Categories"){
                        navController.navigate("allCategory")
                    }
                    else if (actionText == "All Products"){
                        navController.navigate("allProduct")
                    }
                }
        )
    }
}


@Composable
fun PopolarProductSection(popularProduct: List<Product>) {
    Column {
        popularProduct.chunked(2).forEach { rowProducts ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                rowProducts.forEach { product ->
                    ProductCard(
                        imageRes = product.imageRes,
                        title = product.title,
                        price = product.price,
                        rating = "5",
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 8.dp)
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                    )
                }
                if (rowProducts.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@Composable
fun ShopSection(shops: List<Shop>) {
    LazyRow(
    )
    {
        items(shops){ shop->
            ShopCard(
                imageRes = shop.imageRes,
                shopName = shop.shopName,
                shopLocation = shop.shopLocation,
                totalShopProduct = shop.totalShopProduct,
                modifier = Modifier
                    .width(200.dp)
                    .padding(end = 10.dp)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp)) 
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
            )
        }
    }
}

@Composable
fun CategorySection(categories: List<Category>) {
    LazyRow (
    ){
        items(categories) { category ->
            Column (
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ){
                Image(
                    painter = painterResource(id = category.imageRes),
                    contentDescription = category.catName,
                    modifier = Modifier
                        .size(80.dp)
                        .padding(5.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Text(
                    text = category.catName,
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

