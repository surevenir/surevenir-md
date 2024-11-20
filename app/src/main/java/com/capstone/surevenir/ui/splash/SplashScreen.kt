package com.capstone.surevenir.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.capstone.surevenir.R
import com.capstone.surevenir.helper.UserPreferences
import com.capstone.surevenir.ui.theme.CustomFontFamily
import com.capstone.surevenir.ui.theme.MyAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
fun SplashScreen(navigateToHome: () -> Unit, navigateToSignIn: () -> Unit) {
    val context = LocalContext.current
    val userPreferences = UserPreferences(context)
    var isLoggedIn by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        userPreferences.isLoggedIn.collect { loginState ->
            isLoggedIn = loginState
            isLoading = false
        }
    }

    LaunchedEffect(isLoading) {
        if (!isLoading) {
            if (isLoggedIn) {
                navigateToHome()
            } else {
                navigateToSignIn()
            }
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
                painter = painterResource(id = R.drawable.logosure),
                contentDescription = "Logo",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "SureVenir",
                fontFamily = sfui_semibold,
                style = MaterialTheme.typography.h4,
                color = Color.Black,
            )
        }
    }
}
