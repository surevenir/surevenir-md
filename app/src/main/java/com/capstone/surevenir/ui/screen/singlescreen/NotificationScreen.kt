package com.capstone.surevenir.ui.screen.singlescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.capstone.surevenir.R
import com.capstone.surevenir.data.entity.NotificationDatabase
import com.capstone.surevenir.ui.screen.navmenu.sfui_med
import com.capstone.surevenir.ui.screen.navmenu.sfui_semibold
import com.capstone.surevenir.ui.screen.navmenu.sfui_text
import com.capstone.surevenir.ui.viewmodel.NotificationViewModel

@Composable
fun NotificationScreen(
    navController: NavHostController,
    notificationViewModel: NotificationViewModel = hiltViewModel()
) {
    val notifications by notificationViewModel.allNotifications.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Bagian header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFE7E7E9), CircleShape)
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
                text = "Notification",
                fontSize = 25.sp,
                fontFamily = sfui_semibold,
                color = Color(0xFFCC5B14)
            )
        }


        if (notifications.isEmpty()) {
            // Empty state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_notifications_24), // Ganti dengan icon notifikasi yang sesuai
                        contentDescription = "No notifications",
                        modifier = Modifier.size(64.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No notifications yet",
                        fontSize = 18.sp,
                        fontFamily = sfui_med,
                        color = Color.Gray
                    )
                }
            }
        } else {
            if (notifications.any { !it.isRead }) {
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Mark All As Read",
                    fontSize = 20.sp,
                    color = Color(0xFFCC5B14),
                    fontFamily = sfui_semibold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                        .clickable {
                            notificationViewModel.markAllAsRead()
                        }
                )
            }
        }


            LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            items(notifications) { notificationItem: NotificationDatabase ->
                NotificationItem(
                    notification = notificationItem,
                    onClick = {
                        navController.navigate("market/${notificationItem.numericId}")
                        notificationViewModel.markAsRead(notificationItem.id)
                    }
                )
            }
        }
    }
}

@Composable
fun NotificationItem(
    notification: NotificationDatabase,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp)
    ) {
        Text(
            text = notification.title ?: "No Title",
            fontSize = 16.sp,
            fontFamily = sfui_semibold,
            color = Color.Black
        )
        Text(
            text = notification.message ?: "No Message",
            fontSize = 14.sp,
            fontFamily = sfui_text,
            color = Color.Gray
        )

        if (!notification.isRead) {
            Text(
                text = "New",
                fontFamily = sfui_med,
                fontSize = 12.sp,
                color = Color.Red
            )
        }

        Text(
            text = notification.timestamp.toString(),
            fontSize = 12.sp,
            fontFamily = sfui_text,
            color = Color.LightGray
        )
    }
}
