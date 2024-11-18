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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.capstone.surevenir.R
import com.capstone.surevenir.ui.theme.CustomFontFamily
import com.capstone.surevenir.ui.theme.MyAppTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navigateToNextScreen: () -> Unit) {

    val alpha = remember { Animatable(0f) }


    LaunchedEffect(Unit) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1500)
        )
        delay(2000)
        navigateToNextScreen()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8E7DA)), // Warna latar belakang mirip seperti pada gambar
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.logosure), // Ganti dengan ID logo Anda
                contentDescription = "Logo",
                modifier = Modifier.size(100.dp) // Sesuaikan ukuran logo
            )
            Spacer(modifier = Modifier.height(16.dp)) // Jarak antara logo dan teks
            Text(
                text = "SureVenir",
                fontFamily = CustomFontFamily,
                style = MaterialTheme.typography.h4,
                color = Color.Black,
            )
        }
    }
}

@Composable
fun SplashScreenTextPrev() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF)), // Warna latar belakang mirip seperti pada gambar
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.logosure), // Ganti dengan ID logo Anda
                contentDescription = "Logo",
                modifier = Modifier.size(100.dp) // Sesuaikan ukuran logo
            )
            Spacer(modifier = Modifier.height(16.dp)) // Jarak antara logo dan teks
            Text(
                text = "SureVenir",
                fontFamily = CustomFontFamily,
                style = MaterialTheme.typography.h4,
                color = Color.Black,
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    MyAppTheme {
        SplashScreenTextPrev()
    }
}

