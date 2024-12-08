package com.capstone.surevenir.ui.camera

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.ui.Alignment
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.capstone.surevenir.data.network.response.CategoryPrediction
import com.capstone.surevenir.data.network.response.Prediction
import com.capstone.surevenir.data.network.response.RelatedProduct
import com.capstone.surevenir.helper.formatPriceRange
import com.capstone.surevenir.ui.screen.navmenu.sfui_med
import com.capstone.surevenir.ui.screen.navmenu.sfui_semibold

@Composable
fun ResultScreen(
    navController: NavController,
    imageCaptureViewModel: ImageCaptureVM
) {
    val predictionResult = imageCaptureViewModel.predictionResult.collectAsState()

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
            fontFamily = sfui_semibold,
            fontWeight = FontWeight.ExtraBold,
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
                    .padding(12.dp)
                    .size(50.dp)
                    .background(
                        color = Color(0xFFFF8C00),
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                Icon(
                    Icons.Default.BookmarkBorder,
                    contentDescription = "Bookmark",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Results based on your scan:",
            color = Color.Gray,
            fontSize = 14.sp,
            fontFamily = sfui_semibold,
            fontWeight = FontWeight.ExtraBold
        )

        Text(
            text = "[${(prediction.accuration * 100).toInt()}%] ${prediction.result}",
            color = Color(0xFFED8A00),
            fontSize = 22.sp,
            fontFamily = sfui_semibold,
            maxLines = 1,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(vertical = 12.dp)
        )
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Price Range:",
                fontSize = 16.sp,
                fontFamily = sfui_med,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = formatPriceRange(category.rangePrice),
                fontSize = 16.sp,
                fontFamily = sfui_semibold,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )
        }

        Text(
            text = category.description,
            color = Color.Gray,
            fontSize = 14.sp,
            fontFamily = sfui_med,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Text(
            text = "Category",
            fontSize = 16.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontFamily = sfui_med,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                navController.navigate("category/${category.id}")
            },
            modifier = Modifier
                .wrapContentWidth()
                .padding(bottom = 16.dp)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF8C00)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = category.name,
                color = Color.White,
                fontSize = 16.sp,
                fontFamily = sfui_semibold,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Related Products",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = sfui_semibold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        val limitedProducts = relatedProducts
            .groupBy { it.merchant.id }
            .flatMap { (_, products) -> products.take(2) }
            .take(6)

        Column(
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            for (i in limitedProducts.indices step 2) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ProductCardScan(
                        product = limitedProducts[i],
                        modifier = Modifier.weight(1f),
                        navController = navController
                    )
                    if (i + 1 < limitedProducts.size) {
                        ProductCardScan(
                            product = limitedProducts[i + 1],
                            modifier = Modifier.weight(1f),
                            navController = navController
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}


@Composable
private fun ProductCardScan(
    product: RelatedProduct,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(end = 10.dp)
            .clickable {
                navController.navigate("product/${product.id}")
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                AsyncImage(
                    model = product.images.firstOrNull(),
                    contentDescription = "Product Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp)
            ) {

                Text(
                    text = product.name,
                    fontFamily = sfui_semibold,
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Rp ${product.price}",
                    fontFamily = sfui_semibold,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = product.merchant.profile_image_url,
                    contentDescription = "Store Avatar",
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = product.merchant.name,
                    fontFamily = sfui_semibold,
                    fontSize = 10.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
