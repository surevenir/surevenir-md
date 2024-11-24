package com.capstone.surevenir.ui.splash

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.capstone.surevenir.ui.camera.ImageCaptureVM
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.ui.Alignment
import androidx.compose.foundation.background
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

@Composable
fun ResultScreen(
    navController: NavController,
    imageCaptureViewModel: ImageCaptureVM
) {
    ResultScreenContent(
        navController = navController,
        imageUri = imageCaptureViewModel.currentImageUri.toString()
    )
}

@Composable
private fun ResultScreenContent(
    navController: NavController,
    imageUri: String?
) {
    val categories = listOf("Furniture", "Art", "Cultural")
    val stores = listOf(
        Store("Made Art Shop", "store_avatar_1"),
        Store("Putu Furniture", "store_avatar_2")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
    ) {
        // Judul berada di tengah
        Text(
            text = "Identified Souvenir",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFF8C00),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp))
        ) {
            AsyncImage(
                model = imageUri,
                contentDescription = "Identified Souvenir",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            IconButton(
                onClick = { /* Bookmark functionality */ },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    Icons.Default.Bookmark,
                    contentDescription = "Bookmark",
                    tint = Color(0xFFFF8C00)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Results based on your scan:",
            color = Color.Gray,
            fontSize = 14.sp
        )

        Text(
            text = "[90%] Omkara Wall Decoration",
            color = Color(0xFFFF8C00),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Text(
            text = "Price: 100K IDR - 400K IDR",
            fontSize = 16.sp,
            color = Color.Black
        )

        Text(
            text = "The Omkara Wall Decoration is a stunning piece of art that embodies spirituality and elegance, perfect for enhancing your living space with a touch of culture and serenity. Carefully handcrafted by skilled artisans, this wall decor features the sacred Om symbol, representing peace, balance, and universal harmony.",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Text(
            text = "Category",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFFF8C00)
                ) {
                    Text(
                        text = category,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Available at These Stores",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        stores.forEach { store ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    modifier = Modifier.size(24.dp)
                ) {
                    // Placeholder for store avatar
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.LightGray)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = store.name)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ResultScreenPreview() {
    val navController = rememberNavController()
    ResultScreenContent(
        navController = navController,
        imageUri = null
    )
}

data class Store(
    val name: String,
    val avatarUrl: String
)
