package com.capstone.surevenir.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.capstone.surevenir.R
import com.capstone.surevenir.data.network.response.ImageData
import com.capstone.surevenir.data.network.response.ProductData
import com.capstone.surevenir.helper.formatPrice
import com.capstone.surevenir.model.Product
import com.capstone.surevenir.ui.screen.navmenu.sfui_med
import com.capstone.surevenir.ui.screen.navmenu.sfui_semibold
import com.capstone.surevenir.ui.screen.navmenu.sfui_text
import java.text.DecimalFormat

@Composable
fun ProductCard(
    product: ProductData,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(180.dp)  // Set fixed width
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)  // Set fixed height for image
                    .clip(RoundedCornerShape(12.dp))
            ) {
                val imageUrl = if (product.images.isNotEmpty()) {
                    (product.images[0] as? ImageData)?.url ?: "https://via.placeholder.com/150"
                } else {
                    "https://via.placeholder.com/150"
                }

                AsyncImage(
                    model = imageUrl,
                    contentDescription = product.name ?: "No name available",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.name ?: "Unnamed Product",
                fontFamily = sfui_med,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,  // Add ellipsis for long text
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Rp ${formatPrice(product.price)}",
                fontFamily = sfui_text,
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}








