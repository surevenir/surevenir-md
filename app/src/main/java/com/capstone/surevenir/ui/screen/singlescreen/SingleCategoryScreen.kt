package com.capstone.surevenir.ui.screen.singlescreen

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.capstone.surevenir.R
import com.capstone.surevenir.model.Product
import com.capstone.surevenir.ui.screen.navmenu.sfui_semibold

@Composable
fun SingleCategoryScreen (navController: NavHostController){
    // Use LazyColumn to make the content scrollable
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    )
    {
        // Header
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
                    .fillMaxWidth() // Memenuhi lebar layar
                    .height(0.4.dp) // Tinggi garis
                    .background(Color.Gray) // Warna garis
            )
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth() // Memenuhi lebar layar
                    .padding(10.dp),
                contentAlignment = Alignment.Center // Menempatkan konten di tengah secara horizontal
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally // Mengatur elemen di dalam Column tetap rata tengah
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
                    androidx.compose.material3.Text(
                        text = "Art",
                        fontSize = 35.sp,
                        fontFamily = sfui_semibold
                    )
                }
            }
        }


        item {

            Text(
                text = "Souvenir List",
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