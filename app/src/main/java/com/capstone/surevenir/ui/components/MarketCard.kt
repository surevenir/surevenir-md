package com.capstone.surevenir.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.capstone.surevenir.R
import com.capstone.surevenir.ui.screen.navmenu.sfui_med
import com.capstone.surevenir.ui.screen.navmenu.sfui_text


@Composable
fun MarketCard(
    imageRes: String,
    marketName: String,
    marketLocation: String,
    marketDescription: String,
    modifier: Modifier
) {
    Log.d("MarketCard", "Rendering MarketCard - Name: $marketName, Location: $marketLocation, Image: $imageRes")
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(5.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val imageToLoad = if (imageRes.isNotEmpty()) imageRes else "https://via.placeholder.com/150"
            Log.d("MarketCard", "Image URL: $imageToLoad")
            AsyncImage(
                model = imageRes.ifEmpty { "https://via.placeholder.com/150" },
                contentDescription = marketName,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = marketName,
                fontSize = 15.sp,
                fontFamily = sfui_med,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(5.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp,),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_location),
                        contentDescription = "Location Icon",
                        tint = Color(0xFFCC5B14)
                    )
                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = marketLocation,
                        fontFamily = sfui_text
                    )
                }
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.icon_souvenir),
//                        contentDescription = "Souvenir Icon",
//                        tint = Color(0xFFCC5B14)
//                    )
//                    Spacer(modifier = Modifier.width(10.dp))
//                    Text(text = marketDescription)
//                }
            }
            Spacer(modifier = Modifier.height(10.dp))

        }
    }
}