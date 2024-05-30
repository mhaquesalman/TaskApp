package com.example.taskapp.screens

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.material.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.taskapp.models.Priority
import com.example.taskapp.models.Task
import com.example.taskapp.screens.components.TaskAppBar
import com.example.taskapp.screens.components.TaskContent
import com.example.taskapp.ui.theme.appBackgroundColor
import com.example.taskapp.utils.Action
import com.example.taskapp.viewmodels.SharedViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TaskScreen(
    navController: NavController,
    taskId: Int,
    sharedViewModel: SharedViewModel
) {
    
    val selectedTask: Task? by sharedViewModel.selectedTask.collectAsState()

    val title: String by sharedViewModel.title
    val description: String by sharedViewModel.description
    val priority: Priority by sharedViewModel.priority

    val context = LocalContext.current

//    LaunchedEffect(key1 = true) {
//        sharedViewModel.getSelectedTask(taskId)
//    }

    LaunchedEffect(key1 = selectedTask) {
        sharedViewModel.getSelectedTask(taskId)
        if (selectedTask != null || taskId == -1)
            sharedViewModel.updateTaskFields(selectedTask)
    }

    Scaffold(
        backgroundColor = MaterialTheme.colorScheme.appBackgroundColor,
        topBar = {
           TaskAppBar(selectedTask = selectedTask) { action ->
               if (action == Action.CLOSE) {
                   navController.popBackStack()
//                   navController.navigate(route = AppScreens.ListScreen.name) {
//                       popUpTo(AppScreens.ListScreen.name) {
//                           inclusive = true
//                       }
//                   }
               }
               else {
                   if (sharedViewModel.validateFields()) {
                       if (action == Action.ADD) {
                          sharedViewModel.action.value = action
                       } else if (action == Action.UPDATE) {
                           sharedViewModel.action.value = action
                       } else if (action == Action.DELETE) {
                           sharedViewModel.action.value = action
                       }
                       navController.popBackStack()
                   }
                   else {
                      displayToast(context)
                   }
               }
           }
        }
    ) {
        TaskContent(
            title = title,
            description = description,
            priority = priority,
            onTitleChange = {
                sharedViewModel.updateTaskTitle(it)
            },
            onDescriptionChange = {
                sharedViewModel.description.value = it
            },
            onPriorityChange = {
                sharedViewModel.priority.value = it
            }
        )
    }

}

fun displayToast(context: Context) {
    Toast.makeText(
        context,
        "Empty Fields!",
        Toast.LENGTH_SHORT
    ).show()
}