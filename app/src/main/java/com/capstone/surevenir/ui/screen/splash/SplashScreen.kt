package com.capstone.surevenir.ui.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.capstone.surevenir.R
import com.capstone.surevenir.helper.UserPreferences
import com.capstone.surevenir.ui.screen.navmenu.sfui_semibold
import kotlinx.coroutines.flow.first

@Composable
fun SplashScreen(
    navigateToHome: () -> Unit,
    navigateToSignIn: () -> Unit,
    navigateToOnboarding: () -> Unit
) {
    val context = LocalContext.current
    val userPreferences = UserPreferences(context)
    var isLoggedIn by remember { mutableStateOf(false) }
    var hasSeenOnboarding by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var hasNavigated by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        userPreferences.isLoggedIn.collect { loginState ->
            isLoggedIn = loginState
            hasSeenOnboarding = userPreferences.hasSeenOnboarding.first()
            isLoading = false
        }
    }

    LaunchedEffect(true) {
        if (!hasNavigated) {
            val isLoggedIn = userPreferences.isLoggedIn.first()
            val hasOnboarded = userPreferences.hasSeenOnboarding.first()

            when {
                isLoggedIn -> {
                    navigateToHome()
                }
                !hasOnboarded -> {
                    navigateToOnboarding()
                }
                else -> {
                    navigateToSignIn()
                }
            }
            hasNavigated = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8E7DA)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.surevenir_logo_home),
                contentDescription = "Logo",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Surevenir",
                fontFamily = sfui_semibold,
                style = MaterialTheme.typography.h4,
                color = Color.Black,
            )
        }
    }
}
