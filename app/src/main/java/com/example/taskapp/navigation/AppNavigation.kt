package com.example.taskapp.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.taskapp.screens.ListScreen
import com.example.taskapp.screens.SplashScreen
import com.example.taskapp.screens.TaskScreen
import com.example.taskapp.viewmodels.SharedViewModel

@Composable
fun AppNavigation(viewModel: SharedViewModel, isDark: Boolean) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppScreens.SplashScreen.name,
        enterTransition = {
            fadeIn(
                animationSpec = tween(500),
            ) + slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(500)
            )
//            EnterTransition.None
        },
        exitTransition = {
            fadeOut(
                animationSpec = tween(500),
            ) + slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(500)
            )
//            ExitTransition.None
        },
        popEnterTransition = {
            fadeIn(
                animationSpec = tween(500),
            ) + slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(500)
            )
        },
        popExitTransition = {
            fadeOut(
                animationSpec = tween(500),
            ) + slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(500)
            )
        }
    ) {

        // Splash screen
        composable(route = AppScreens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }

        // List screen
        composable(route = AppScreens.ListScreen.name) {
            ListScreen(navController = navController, sharedViewModel = viewModel, isDark = isDark)
        }

        // Task screen
        val taskScreen = AppScreens.TaskScreen.name
        composable(route = "$taskScreen/{taskId}",
            arguments = listOf(navArgument("taskId") {
                type = NavType.IntType
            })
        ) {backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId")
            taskId?.let {
                TaskScreen(navController = navController, taskId = it, sharedViewModel = viewModel)
            }
        }
    }
}

