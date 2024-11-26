package com.capstone.surevenir.ui.component

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.capstone.surevenir.R
import com.capstone.surevenir.ui.screen.navmenu.sfui_semibold

@Composable
fun ShopCard (
    imageRes: Int,
    shopName: String,
    shopLocation: String,
    totalShopProduct: Int,
    modifier: Modifier
){
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(20.dp)
    ){
        Column (
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = shopName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = shopName,
                fontFamily = sfui_semibold,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically // Sejajarkan elemen ke tengah secara ve
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_location),
                        contentDescription = "Location Icon",
                        tint = Color(0xFFCC5B14)                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = shopLocation,
                        fontFamily = sfui_semibold,                    )
                }
                Row (
                    verticalAlignment = Alignment.CenterVertically // Sejajarkan elemen ke tengah secara ve
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_souvenir),
                        contentDescription = "Location Icon",
                        tint = Color(0xFFCC5B14)                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = totalShopProduct.toString(),
                        fontFamily = sfui_semibold,                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun previewShopCard(){
    ShopCard(
        imageRes = R.drawable.shop,
        shopName = "Shop Name",
        shopLocation = "Shop Location",
        totalShopProduct = 10,
        modifier = Modifier
    )
}
