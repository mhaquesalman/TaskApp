package com.example.taskapp.screens

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarResult
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.taskapp.R
import com.example.taskapp.models.Priority
import com.example.taskapp.models.Task
import com.example.taskapp.navigation.AppScreens
import com.example.taskapp.screens.components.AppBar
import com.example.taskapp.screens.components.TaskLists
import com.example.taskapp.ui.theme.appBackgroundColor
import com.example.taskapp.ui.theme.fabBackgroundColor
import com.example.taskapp.ui.theme.topAppBarContentColor
import com.example.taskapp.utils.Action
import com.example.taskapp.utils.RequestState
import com.example.taskapp.utils.SearchAppBarState
import com.example.taskapp.viewmodels.SharedViewModel
import androidx.compose.material3.MaterialTheme as MaterialTheme3


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ListScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    isDark: Boolean
) {

    val searchAppBarState: SearchAppBarState = sharedViewModel.searchAppBarState.value
    val searchTextState: String by sharedViewModel.searchTextState

    val action: Action by sharedViewModel.action

    val allTasks: RequestState<List<Task>> by sharedViewModel.allTasks.collectAsState()
    val searchedTasks: RequestState<List<Task>> by sharedViewModel.searchedTasks.collectAsState()
    val lowPriorityTasks: RequestState<List<Task>> by sharedViewModel.lowPriorityTasks.collectAsState()
    val highPriorityTasks: RequestState<List<Task>> by sharedViewModel.highPriorityTasks.collectAsState()

    val sortState: Priority by sharedViewModel.sortState.collectAsState()

    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = sortState) {
        sharedViewModel.readSortState()

        when (sortState) {
            Priority.LOW -> sharedViewModel.sortByLowPriorityTasks()
            Priority.HIGH -> sharedViewModel.sortByHighPriorityTasks()
            else -> sharedViewModel.getAllTasks()
        }
    }

    LaunchedEffect(key1 = action) {
        sharedViewModel.handleDbOps(action)
        if (action != Action.CLOSE && action != Action.NO_ACTION) {
            val actionMessage =
                if (action == Action.DELETE_ALL) "All tasks removed"
                 else "${action.name}: ${sharedViewModel.title.value}"
            val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
                message = actionMessage,
                actionLabel = if (action.name == "DELETE") "UNDO" else "OK"
            )
            if (snackBarResult == SnackbarResult.ActionPerformed && action.name == "DELETE") {
                sharedViewModel.action.value = Action.UNDO
            } else {
                sharedViewModel.action.value = Action.NO_ACTION
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme3.colorScheme.appBackgroundColor,
        topBar = {
            AppBar(
                sharedViewModel = sharedViewModel,
                searchAppBarState = searchAppBarState,
                searchTextState = searchTextState,
                isDark = isDark
            )
        },
        floatingActionButton = {
            FabActionButton {
                val taskScreen = AppScreens.TaskScreen.name
                navController.navigate(route = "$taskScreen/" + -1)
            }
        }
    ) {
        TaskLists(
            allTasks = allTasks,
            searchedTasks = searchedTasks,
            lowPriorityTasks = lowPriorityTasks,
            highPriorityTasks = highPriorityTasks,
            sortState = sortState,
            searchAppBarState = searchAppBarState,
            searchedText = searchTextState,
            onSwipeToDelete = { action, task ->
                sharedViewModel.action.value = action
                sharedViewModel.updateTaskFields(selectedTask = task)
            },
            onCheckboxClicked = { action, task ->
                sharedViewModel.updateCheckedTask(task)
            }
        ) { taskId ->
            val taskScreen = AppScreens.TaskScreen.name
            navController.navigate(route = "$taskScreen/" + taskId)
        }
    }
}

@Composable
fun FabActionButton(onFabClicked: () -> Unit) {
    FloatingActionButton(
        onClick = { onFabClicked() },
        containerColor = MaterialTheme3.colorScheme.fabBackgroundColor
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(id = R.string.add_button),
            tint = MaterialTheme3.colorScheme.topAppBarContentColor
        )
    }
}



/*
@Composable
fun DisplaySnackBar(
    scaffoldState: ScaffoldState,
    action: Action,
    handleDbOps: () -> Unit
) {
    LaunchedEffect(key1 = action) {
        if (action != Action.CLOSE && action != Action.NO_ACTION) {
            val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
                message = "Task ${action.name}",
                actionLabel = "Ok"
            )
        }
    }
}*/
