package com.capstone.surevenir.ui.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import com.capstone.surevenir.ui.components.TopBar

@Composable
fun SettingsScreen(navController: NavHostController) {
    var themeEnabled by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBar(
            title = "Settings",
            onBackClick = { navController.navigateUp() }
        )

        SettingsGroup {
            SettingsItem(
                icon = Icons.Default.Accessibility,
                title = "Accessibility",
                subtitle = "Enhance visibility options",
                onClick = { /* Handle accessibility settings */ }
            )
            SettingsItem(
                icon = Icons.Default.Language,
                title = "Language",
                subtitle = "Set preferred language",
                onClick = { /* Handle language settings */ }
            )
            SettingsItem(
                icon = Icons.Default.Settings,
                title = "Device Permissions",
                subtitle = "Manage app permissions",
                onClick = { /* Handle permissions */ }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        SettingsGroup {
            SettingsSwitchItem(
                icon = Icons.Default.Palette,
                title = "Change Theme",
                subtitle = "Switch between light and dark mode",
                checked = themeEnabled,
                onCheckedChange = { themeEnabled = it }
            )
            SettingsSwitchItem(
                icon = Icons.Default.Notifications,
                title = "Notifications",
                subtitle = "Enable or disable notifications",
                checked = notificationsEnabled,
                onCheckedChange = { notificationsEnabled = it }
            )
        }
    }
}

@Composable
private fun SettingsGroup(
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        content()
    }
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title)
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}

@Composable
private fun SettingsSwitchItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title)
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFFED8A00)
            )
        )
    }
}