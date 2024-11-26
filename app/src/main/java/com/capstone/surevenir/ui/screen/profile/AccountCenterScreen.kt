package com.capstone.surevenir.ui.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.ui.Alignment
import com.capstone.surevenir.ui.components.TopBar

@Composable
fun AccountCenterScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBar(
            title = "Account Center",
            onBackClick = { navController.navigateUp() }
        )

        Text(
            text = "SureVenir utilizes this data to authenticate your identity and ensure the safety of our community",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column {
                AccountField(
                    label = "Email",
                    value = "audryjenner@gmail.com",
                    onClick = { /* Handle email edit */ }
                )
                HorizontalDivider()
                AccountField(
                    label = "Phone Number",
                    value = "+62 812 3456 6778",
                    onClick = { /* Handle phone edit */ }
                )
                HorizontalDivider()
                AccountField(
                    label = "Password",
                    value = "***********",
                    onClick = { /* Handle password edit */ }
                )
            }
        }
    }
}

@Composable
private fun AccountField(
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Edit",
            tint = Color.Gray
        )
    }
}
