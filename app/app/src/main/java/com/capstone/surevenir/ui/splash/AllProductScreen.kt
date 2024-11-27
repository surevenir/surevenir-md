package com.capstone.surevenir.ui.splash

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.capstone.surevenir.R
import com.capstone.surevenir.components.ProductCard
import com.capstone.surevenir.model.Product

@Composable
fun AllProductScreen(navController: NavHostController) {

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
        ),
        Product(
            R.drawable.product_image,
            "Beach Hat",
            "Elegant hat for sunny days.",
            "IDR 75.000"
        ),
        Product(
            R.drawable.product_image,
            "Coffee Mug",
            "Stylish Bali-themed coffee mug.",
            "IDR 50.000"
        ),
        Product(
            R.drawable.product_image,
            "Fridge Magnet",
            "Custom Bali fridge magnet.",
            "IDR 30.000"
        ),
        Product(
            R.drawable.product_image,
            "Beach Bag",
            "Perfect for your Bali vacation.",
            "IDR 100.000"
        )
    )

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

        AllProductSection(popularProducts, navController)
    }
}

@Composable
fun AllProductSection(popularProducts: List<Product>, navController: NavHostController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        items(popularProducts.chunked(2)) { rowProducts ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
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
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .padding(8.dp)
                            .clickable { navController.navigate("singleProduct")  }
                    )
                }
                if (rowProducts.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewAllProductScreen() {
    val navController = rememberNavController()
    AllProductScreen(navController = navController)
}