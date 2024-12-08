package com.capstone.surevenir.ui.camera

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.ui.Alignment
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.capstone.surevenir.data.network.response.CategoryPrediction
import com.capstone.surevenir.data.network.response.Prediction
import com.capstone.surevenir.data.network.response.RelatedProduct

@Composable
fun ResultScreen(
    navController: NavController,
    imageCaptureViewModel: ImageCaptureVM
) {
    val predictionResult = imageCaptureViewModel.predictionResult.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            imageCaptureViewModel.clearPredictionResult()
        }
    }

    predictionResult.value?.fold(
        onSuccess = { response ->
            val data = response.data
            ResultScreenContent(
                navController = navController,
                imageUrl = data.imageUrl,
                prediction = data.prediction,
                category = data.category,
                relatedProducts = data.relatedProducts
            )
        },
        onFailure = {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Failed to load prediction result: ${it.message}")
            }
        }
    )
}

@Composable
private fun ResultScreenContent(
    navController: NavController,
    imageUrl: String,
    prediction: Prediction,
    category: CategoryPrediction,
    relatedProducts: List<RelatedProduct>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
            .verticalScroll(rememberScrollState())
    ) {
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
                model = imageUrl,
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
            text = "[${(prediction.accuration * 100).toInt()}%] ${prediction.result}",
            color = Color(0xFFED8A00),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Text(
            text = "Price Range: ${category.rangePrice}",
            fontSize = 16.sp,
            color = Color.Black
        )

        Text(
            text = category.description,
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


        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Available at These Stores",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        relatedProducts.distinctBy { it.merchant.id }.take(3).forEach { product ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = product.merchant.profile_image_url,
                    contentDescription = "Store Avatar",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = product.merchant.name,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Stock: ${product.stock}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Rp ${product.price}",
                    color = Color(0xFFED8A00),
                    fontWeight = FontWeight.Bold
                )


                Spacer(modifier = Modifier.height(16.dp))

                // Similar Products
                Text(
                    text = "Similar Products",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(relatedProducts) { product ->
                        Card(
                            modifier = Modifier
                                .width(160.dp)
                                .clickable { /* Navigate to product detail */ }
                        ) {
                            Column {
                                AsyncImage(
                                    model = product.images.firstOrNull(),
                                    contentDescription = "Product Image",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp),
                                    contentScale = ContentScale.Crop
                                )
                                Column(
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Text(
                                        text = product.name,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "Rp ${product.price}",
                                        color = Color(0xFFED8A00),
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = product.merchant.name,
                                        color = Color.Gray,
                                        fontSize = 12.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
