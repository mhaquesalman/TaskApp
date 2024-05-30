package com.example.taskapp.navigation

enum class AppScreens {
    SplashScreen,
    ListScreen,
    TaskScreen;

    companion object {
        fun fromRoute(route: String?) =
            when (route?.substringBefore("/")) {
                SplashScreen.name -> SplashScreen
                ListScreen.name -> ListScreen
                TaskScreen.name -> TaskScreen
                null -> ListScreen
                else -> throw IllegalArgumentException("Route $route is not recognized!")
            }
    }
}

