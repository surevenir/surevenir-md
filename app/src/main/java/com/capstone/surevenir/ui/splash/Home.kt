package com.capstone.surevenir.ui.splash

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.capstone.surevenir.R

@Composable
fun Home(navController: NavController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFCEFE3)) // Warna latar belakang halaman
            .padding(horizontal = 16.dp)
    ) {
        // Top Bar
        TopBar()

        // Hero Section
        HeroSection()

        Spacer(modifier = Modifier.height(16.dp))

        // Scan History Section
        SectionTitle(title = "Scan History", seeAllAction = { /* Handle See All */ })
        ScanHistoryList()

        Spacer(modifier = Modifier.height(16.dp))

        // All Products Section
        SectionTitle(title = "All Products", seeAllAction = { /* Handle See All */ })
        ProductGrid()

        // Bottom Navigation
        Spacer(modifier = Modifier.weight(1f))
        BottomNavigationBar()
    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.logo), // Ganti dengan logo Anda
            contentDescription = "Logo",
            modifier = Modifier.size(32.dp)
        )

        // Location
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Your Location", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Ubud ▼",
                color = Color(0xFFFFA726),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun HeroSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFFA726)) // Warna background banner
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Gambar souvenir
            Image(
                painter = painterResource(id = R.drawable.souvenir_image), // Ganti dengan gambar
                contentDescription = "Souvenir",
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Teks
            Column {
                Text(
                    text = "Find The Souvenir",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
                Text(
                    text = "Scan it and get the info details!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { /* Handle Click */ },
                    colors = ButtonDefaults.buttonColors(contentColor = Color.White)
                ) {
                    Text(text = "Click The Button", color = Color(0xFFFFA726))
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String, seeAllAction: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        Text(
            text = "See all",
            color = Color(0xFFFFA726),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.clickable { seeAllAction() }
        )
    }
}

@Composable
fun ScanHistoryList() {
    LazyRow {
        items(5) { // Ganti dengan data asli
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .padding(end = 16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(8.dp)
            ) {
                Column {
                    Image(
                        painter = painterResource(id = R.drawable.product_image), // Ganti dengan gambar produk
                        contentDescription = "Product Image",
                        modifier = Modifier.height(100.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Dream Catcher", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = "IDR 50.000 - 150.000",
                        color = Color(0xFFFFA726),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun ProductGrid() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(10) { // Ganti dengan data asli
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(8.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.product_image), // Ganti dengan gambar
                        contentDescription = "Product Image",
                        modifier = Modifier.height(100.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Bali Hand Magnet", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = "IDR 25.000",
                        color = Color(0xFFFFA726),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painter = painterResource(id = R.drawable.ic_home), contentDescription = "Home")
        Icon(painter = painterResource(id = R.drawable.ic_shop), contentDescription = "Shop")
        Icon(painter = painterResource(id = R.drawable.ic_scan), contentDescription = "Scan")
        Icon(painter = painterResource(id = R.drawable.ic_favorite), contentDescription = "Favorites")
        Icon(painter = painterResource(id = R.drawable.ic_profile), contentDescription = "Profile")
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    Home(navController = NavController(LocalContext.current))
}


