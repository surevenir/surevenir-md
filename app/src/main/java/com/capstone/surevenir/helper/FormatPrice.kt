package com.capstone.surevenir.helper

import java.text.DecimalFormat

fun formatPrice(price: Int?): String {
    val decimalFormat = DecimalFormat("#,###")
    return decimalFormat.format(price ?: 0)
}