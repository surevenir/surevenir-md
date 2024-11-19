package com.capstone.surevenir.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.capstone.surevenir.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = Color(0xFF8E3200),
    primaryVariant = Color(0xFFCC5B14),
    secondary = Color(0xFFED8A00),
    secondaryVariant = Color(0xFFFFC87B)
)

private val LightColorPalette = lightColors(
    primary = Color(0xFF8E3200),
    primaryVariant = Color(0xFFCC5B14),
    secondary = Color(0xFFED8A00),
    secondaryVariant = Color(0xFFFFC87B)
)

val CustomFontFamily = FontFamily(
    Font(R.font.poppinssemibold, FontWeight.Normal) // Sesuaikan dengan nama font Anda
)

val Typography = Typography(
    h4 = TextStyle(
        fontFamily = CustomFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = CustomFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
)

@Composable
fun MyAppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    // Mengontrol Status Bar dan Navigation Bar
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !darkTheme

    SideEffect {
        // Mengatur warna Status Bar
        systemUiController.setStatusBarColor(
            color = colors.secondaryVariant,
            darkIcons = useDarkIcons
        )
        // Mengatur warna Navigation Bar (opsional)
        systemUiController.setNavigationBarColor(
            color = colors.secondaryVariant,
            darkIcons = useDarkIcons
        )
    }

    MaterialTheme(
        colors = colors,
        content = content
    )
}
