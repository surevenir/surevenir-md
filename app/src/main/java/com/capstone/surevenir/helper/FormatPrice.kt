package com.capstone.surevenir.helper

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

fun formatPrice(price: Int?): String {
    val decimalFormat = DecimalFormat("#,###")
    return decimalFormat.format(price ?: 0)
}

fun formatCurrency(price: Int?): String {
    val locale = Locale("in", "ID")

    val numberFormat = NumberFormat.getCurrencyInstance(locale).apply {
        minimumFractionDigits = 0
        maximumFractionDigits = 0
    }

    val formattedPrice = numberFormat.format(price ?: 0)
    return formattedPrice
}

fun formatPriceRange(priceRange: String?): String {
    if (priceRange.isNullOrBlank()) return "N/A"

    val parts = priceRange.split(" - ")

    return if (parts.size == 2) {
        val startPrice = parts[0].toIntOrNull()
        val endPrice = parts[1].toIntOrNull()

        if (startPrice != null && endPrice != null) {
            "${formatCurrency(startPrice)} - ${formatCurrency(endPrice)}"
        } else {
            "Invalid range"
        }
    } else {
        "Invalid range"
    }
}