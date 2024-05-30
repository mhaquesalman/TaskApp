package com.example.taskapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.taskapp.navigation.AppNavigation
import com.example.taskapp.ui.theme.TaskAppTheme
import com.example.taskapp.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val sharedViewModel: SharedViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel.readThemeState()
        setContent {
            MyApp(viewModel = sharedViewModel)
        }
    }
}

@Composable
fun MyApp(viewModel: SharedViewModel) {
    val isDark by  viewModel.themeState.collectAsState()
//    val themeState by derivedStateOf {
//        isDark = viewModel.themeState.value
//        isDark
//    }
    Log.d("DebugTaskApp", "Theme: $isDark")

    TaskAppTheme(darkTheme = isDark) {
        AppNavigation(viewModel = viewModel, isDark = isDark)
    }
}


