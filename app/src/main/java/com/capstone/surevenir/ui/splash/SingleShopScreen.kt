package com.capstone.surevenir.ui.splash

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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

@Composable
fun SingleShopScreen(navController: NavHostController) {

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
                    text = "Detail Shop",
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Gambar Toko
                Image(
                    painter = painterResource(id = R.drawable.shop),
                    contentDescription = "Shop Image",
                    modifier = Modifier
                        .size(150.dp) // Ukuran gambar sesuai contoh
                        .background(Color.White)
                )

                Spacer(modifier = Modifier.width(12.dp)) // Jarak antara gambar dan teks

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Nama Toko
                    Text(
                        text = "Made Art Shop",
                        fontFamily = sfui_semibold,
                        fontSize = 30.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Informasi Barang
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_souvenir), // Ikon untuk jumlah item
                            contentDescription = "Items Icon",
                            tint = Color(0xFFCC5B14),
                            modifier = Modifier.size(30.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "   20 items",
                            fontFamily = sfui_med,
                            fontSize = 18.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Lokasi
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_location), // Ikon untuk lokasi
                            contentDescription = "Location Icon",
                            tint = Color(0xFFCC5B14),
                            modifier = Modifier.size(30.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "   Ubud",
                            fontFamily = sfui_med,
                            fontSize = 18.sp,
                            color = Color.Gray,
                        )
                    }
                }
            }
            Spacer(
                modifier = Modifier
                    .padding(top = 15.dp, end = 20.dp, start = 20.dp)
                    .fillMaxWidth() // Memenuhi lebar layar
                    .height(0.4.dp) // Tinggi garis
                    .background(Color.Gray) // Warna garis
            )
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