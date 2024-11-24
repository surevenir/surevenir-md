package com.capstone.surevenir.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.capstone.surevenir.R
import com.capstone.surevenir.ui.splash.sfui_semibold
import com.capstone.surevenir.ui.splash.sfui_text

@Composable
fun ScanHistoryCard(
    imageRes: Int,
    title: String,
    description: String,
    price: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                fontFamily = sfui_semibold,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                fontFamily = sfui_text,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .align(Alignment.Start)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFFA726))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = price,
                    fontFamily = sfui_semibold,
                    fontSize = 10.sp
                )
            }
        }
    }
}
