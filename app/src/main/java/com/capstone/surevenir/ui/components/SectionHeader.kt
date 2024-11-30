package com.capstone.surevenir.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.capstone.surevenir.ui.screen.navmenu.sfui_semibold

@Composable
fun SectionHeader(title: String, actionText: String, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        androidx.compose.material.Text(
            text = title,
            fontSize = 20.sp,
            fontFamily = sfui_semibold
        )
        androidx.compose.material.Text(
            text = actionText,
            fontSize = 20.sp,
            fontFamily = sfui_semibold,
            color = Color(0xFFCC5B14),
            modifier = Modifier
                .clickable {
                    if (actionText == "All Shops"){
                        navController.navigate("allShop")
                    }
                    else if (actionText == "All Categories"){
                        navController.navigate("allCategory")
                    }
                    else if (actionText == "All Markets" || actionText == "See All Markets"){
                        navController.navigate("allMarket")
                    }
                    else if (actionText == "All Products" || actionText == "See All Products"){
                        navController.navigate("allProduct")
                    }
                    else if (actionText == "All History"){
                        navController.navigate("allHistory")
                    }
                }
        )
    }
}
