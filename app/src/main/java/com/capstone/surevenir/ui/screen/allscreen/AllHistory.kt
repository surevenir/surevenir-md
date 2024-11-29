package com.capstone.surevenir.ui.screen.allscreen

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.capstone.surevenir.R
import com.capstone.surevenir.ui.components.ScanHistoryCard
import com.capstone.surevenir.model.Product
import com.capstone.surevenir.ui.screen.navmenu.sfui_semibold

@Composable
fun AllHistory(navController: NavHostController) {
//    val scanHistory = listOf(
//        Product(
//            R.drawable.product_image,
//            "Bali Hand Magnet",
//            "Magnet souvenir scanned in Bali.",
//            "IDR 25.000"
//        ),
//        Product(
//            R.drawable.product_image,
//            "Keychain",
//            "Customizable keychain scanned in Seminyak.",
//            "IDR 15.000"
//        ),
//        Product(
//            R.drawable.product_image,
//            "T-shirt Bali",
//            "Bali-themed T-shirt scanned in Ubud.",
//            "IDR 150.000"
//        ),
//        Product(
//            R.drawable.product_image,
//            "Beach Hat",
//            "Elegant hat scanned in Kuta.",
//            "IDR 75.000"
//        )
//        ,
//        Product(
//            R.drawable.product_image,
//            "Beach Hat",
//            "Elegant hat scanned in Kuta.",
//            "IDR 75.000"
//        ),
//        Product(
//            R.drawable.product_image,
//            "Beach Hat",
//            "Elegant hat scanned in Kuta.",
//            "IDR 75.000"
//        ),
//        Product(
//            R.drawable.product_image,
//            "Beach Hat",
//            "Elegant hat scanned in Kuta.",
//            "IDR 75.000"
//        ),
//        Product(
//            R.drawable.product_image,
//            "Beach Hat",
//            "Elegant hat scanned in Kuta.",
//            "IDR 75.000"
//        )

//    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
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
                text = "Scan History",
                fontSize = 25.sp,
                fontFamily = sfui_semibold,
                color = Color(0xFFCC5B14)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

//        AllScanHistorySection(scanHistory)
    }
}

@Composable
fun AllScanHistorySection(products: List<Product>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
//        items(products) { product ->
//            ScanHistoryCard(
//                imageRes = product.imageRes,
//                title = product.title,
//                description = product.description,
//                price = product.price,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp))
//                    .clip(RoundedCornerShape(8.dp))
//                    .background(Color.White)
//            )
//        }
    }
}
