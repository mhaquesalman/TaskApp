package com.example.taskapp.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import com.example.taskapp.utils.isLightTheme


val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val Teal500 = Color(0xFF168B72)

val Purple40 = Color(0xFF6650a4)
val Purple500 = Color(0xFF6200EE)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val LightGray = Color(0xFFD5D2D2)
val MediumGray = Color(0xFF9C9C9C)
val DarkGray = Color(0xFF424242)

val LowPriorityColor = Color(0xFF00C980)
val MediumPriorityColor = Color(0xFFFFC114)
val HighPriorityColor = Color(0xFFFF4646)
val NoPriorityColor = Color(0xFF9E9A9A)

val ColorScheme.appBackgroundColor: Color
    get() = if (isLightTheme()) Color.White else DarkGray

val ColorScheme.topAppBarContentColor: Color
    @Composable
    get() {
        return if (isLightTheme()) Color.White else Color.Black
    }

val ColorScheme.topAppBarBackgroundColor: Color
    @Composable
    get() {
        return if (isLightTheme()) Purple500 else Teal500
    }

val ColorScheme.fabBackgroundColor: Color
    @Composable
    get() {
        val isLight = background.luminance() > 0.5
        return if (isLight) Purple500 else Teal500
    }

val ColorScheme.taskContentColor: Color
    get() = if (isLightTheme()) Color.Black else Color.White

val ColorScheme.itemTextColor: Color
    @Composable
    get() = if (isLightTheme()) DarkGray else LightGray
//
val ColorScheme.itemBackgroundColor: Color
    @Composable
    get() = if (isLightTheme()) Color.White else DarkGray


val ColorScheme.splashScreenBackground: Color
    @Composable
    get() = if (isLightTheme()) Purple500 else Teal500

val Colors.taskItemTextColor: Color
    @Composable
    get() = if (isLight) DarkGray else LightGray

val Colors.taskItemBackgroundColor: Color
    @Composable
    get() = if (isLight) Color.White else DarkGray
