package com.example.taskapp.utils

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.luminance
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDate(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("EEE, d MMM hh:mm aaa", Locale.getDefault())
    return formatter.format(date)
}

fun ColorScheme.isLightTheme() = background.luminance() > 0.5