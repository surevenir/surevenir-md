package com.capstone.survenir.model

import androidx.annotation.DrawableRes

data class  OnboardingPage(
    @DrawableRes val imageRes: Int,
    val title: String,
    val description: String,
    val highlightText: String,
    val belowText: String
)
