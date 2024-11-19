package com.capstone.surevenir.ui.splash

import com.capstone.surevenir.R

sealed class BottomNavItem(val route: String, val icon: Int, val title: String) {
    object Home : BottomNavItem("home", R.drawable.ic_home, "Home")
    object Shop : BottomNavItem("shop", R.drawable.ic_shop, "Shop")
    object Scan : BottomNavItem("scan", R.drawable.ic_scan, "Scan")
    object Favorites : BottomNavItem("favorites", R.drawable.ic_favorite, "Favorites")
    object Profile : BottomNavItem("profile", R.drawable.ic_profile, "Profile")
}
