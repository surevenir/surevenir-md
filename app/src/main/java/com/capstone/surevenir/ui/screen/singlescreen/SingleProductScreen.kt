package com.capstone.surevenir.ui.screen.singlescreen


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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavHostController
import com.capstone.surevenir.ui.components.ProductCard
import com.capstone.surevenir.model.Product
import com.capstone.surevenir.ui.screen.navmenu.sfui_med
import com.capstone.surevenir.ui.screen.navmenu.sfui_semibold
import com.capstone.surevenir.ui.screen.navmenu.sfui_text
import kotlinx.coroutines.launch

@Composable
fun SingleProductScreen(navController: NavHostController) {

    val images = listOf(
        R.drawable.product_image,
        R.drawable.product_image,
        R.drawable.product_image,
        R.drawable.product_image
    )


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
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

                ImageSlider(images = images)


            }
        }


        item {
            Column (  modifier = Modifier
                .fillMaxWidth()
            ){

                Text(
                    modifier = Modifier
                        .padding(16.dp),
                    text = "Rp. 150.000",
                    fontSize = 24.sp,
                    fontFamily = sfui_semibold,
                    color = Color.Black
                )


                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.4.dp)
                        .background(Color.Gray)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(modifier = Modifier
                    .padding(start = 10.dp),
                    text = "Bali Hand Magnet",
                    fontSize = 25.sp,
                    fontFamily = sfui_semibold,
                    color = Color.Black
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp, start = 10.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_star),
                        contentDescription = "Rating Star",
                        tint = Color(0xFFFFCC00),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "4.8 (7)",
                        fontSize = 14.sp,
                        fontFamily = sfui_text,
                        color = Color.Black
                    )

                }
                Spacer(modifier = Modifier.height(20.dp))

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(15.dp)
                        .background(Color.LightGray)
                )

            }
        }

        item {
            Column(  modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {
                Text(
                    text = "Details",
                    fontSize = 25.sp,
                    fontFamily = sfui_semibold,
                    color = Color.Black
                )
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(
                        text = "Category: ",
                        fontSize = 18.sp,
                        fontFamily = sfui_text,
                        color = Color.Gray
                    )
                    Text(
                        text = "Magnet",
                        fontSize = 18.sp,
                        fontFamily = sfui_med,
                        color = Color(0xFFFF6600)
                    )
                }
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(
                        text = "Weight: ",
                        fontSize = 18.sp,
                        fontFamily = sfui_text,
                        color = Color.Gray
                    )
                    Text(
                        text = "500 g",
                        fontSize = 18.sp,
                        fontFamily = sfui_med,
                        color = Color.Black
                    )
                }
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(
                        text = "Condition: ",
                        fontSize = 18.sp,
                        fontFamily = sfui_text,
                        color = Color.Gray
                    )
                    Text(
                        text = "New",
                        fontSize = 18.sp,
                        fontFamily = sfui_med,
                        color = Color.Black
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
        }


        item {
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = "Deskripsi produk",
                    fontSize = 18.sp,
                    fontFamily = sfui_semibold,
                    color = Color.Black
                )

                Spacer(Modifier.height(10.dp))
                Text(
                    text = "The Bali Hand Magnet captures Bali’s vibrant culture through intricate hand gestures inspired by traditional dance. Made with detailed craftsmanship, these colorful magnets are perfect keepsakes, adding a touch of Balinese artistry to any metal surface.\n\nThe Bali Hand Magnet captures Bali’s vibrant culture through intricate hand gestures inspired by traditional dance. Made with detailed craftsmanship, these colorful magnets are perfect keepsakes, adding a touch of Balinese artistry to any metal surface.",
                    fontSize = 14.sp,
                    fontFamily = sfui_med,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            Spacer(
                modifier = Modifier
                    .padding(top = 15.dp)
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
                Image(
                    painter = painterResource(id = R.drawable.shop),
                    contentDescription = "Shop Image",
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color.White)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Made Art Shop",
                        fontFamily = sfui_semibold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_souvenir),
                            contentDescription = "Items Icon",
                            tint = Color(0xFFCC5B14),
                            modifier = Modifier.size(16.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "20 items",
                            fontFamily = sfui_med,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_location),
                            contentDescription = "Location Icon",
                            tint = Color(0xFFCC5B14),
                            modifier = Modifier.size(16.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "Ubud",
                            fontFamily = sfui_med,
                            fontSize = 14.sp,
                            color = Color.Gray,
                        )
                    }
                }
            }
            Spacer(
                modifier = Modifier
                    .padding(top = 15.dp)
                    .fillMaxWidth()
                    .height(12.dp)
                    .background(Color.LightGray)
            )
        }


        item {
            Column(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
                Text(
                    text = "Reviews",
                    fontSize = 25.sp,
                    fontFamily = sfui_semibold,
                    color = Color.Black
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_star),
                        contentDescription = "Rating Star",
                        tint = Color(0xFFFFCC00),
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "4.8/5.0 (7 review)",
                        fontSize = 18.sp,
                        fontFamily = sfui_med,
                        color = Color.Black
                    )

                }
                Spacer(
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .fillMaxWidth()
                        .height(0.4.dp)
                        .background(Color.Gray)
                )
                Spacer(modifier = Modifier.height(8.dp))
                repeat(2) {
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        Text(
                            text = "Abdul",
                            fontSize = 16.sp,
                            fontFamily = sfui_semibold,
                            color = Color.Black
                        )
                        Text(
                            text = "20 April 2024",
                            fontSize = 12.sp,
                            fontFamily = sfui_text,
                            color = Color.Gray
                        )
                        Text(
                            text = "The Bali Hand Magnet captures Bali’s vibrant culture through intricate hand gestures inspired by traditional dance.",
                            fontSize = 14.sp,
                            fontFamily = sfui_text,
                            color = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Text(
                    text = "See All Reviews",
                    fontSize = 18.sp,
                    fontFamily = sfui_semibold,
                    color = Color(0xFFFF6600),
                    modifier = Modifier.clickable { /* Navigate to All Reviews */ }
                )
            }
            Spacer(
                modifier = Modifier
                    .padding(top = 15.dp)
                    .fillMaxWidth()
                    .height(12.dp)
                    .background(Color.LightGray)
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Other Souvenir You Might Like",
                fontSize = 20.sp,
                fontFamily = sfui_semibold,
                color = Color.Black,
                modifier = Modifier
                    .padding(16.dp)
            )
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
                ProductSectionSix(popularProduct = popularProducts)
            }
        }
    }


@Composable
fun ProductSectionSix(popularProduct: List<Product>) {
    Column (
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
    ){
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageSlider(images: List<Int>) {
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
                painter = painterResource(id = images[page]),
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
