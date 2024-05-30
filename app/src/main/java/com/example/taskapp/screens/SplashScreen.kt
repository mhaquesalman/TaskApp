package com.example.taskapp.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.taskapp.R
import com.example.taskapp.navigation.AppScreens
import com.example.taskapp.ui.theme.LOGO_SIZE
import com.example.taskapp.ui.theme.splashScreenBackground
import com.example.taskapp.utils.Constants
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController
) {

    var startAnimation by remember { mutableStateOf(false) }
    val offsetState by animateDpAsState(
        targetValue = if (startAnimation) 0.dp else 100.dp,
        animationSpec = tween(1000)
    )

    val alphaState by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(1000)
    )

    LaunchedEffect(key1 = Unit){
        startAnimation = true
        delay(Constants.SPLASH_SCREEN_DELAY)
//        navController.popBackStack()
        navController.navigate(route = AppScreens.ListScreen.name){
            popUpTo(route = AppScreens.SplashScreen.name) {
                inclusive = true
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.splashScreenBackground),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .size(LOGO_SIZE)
                .offset(y = offsetState)
                .alpha(alpha = alphaState),
            painter = painterResource(
                id = if (isSystemInDarkTheme()) R.drawable.logo_dark
                else R.drawable.logo_light
            ),
            contentDescription = "TASK LOGO"
        )
    }
}
