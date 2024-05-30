@file:OptIn(ExperimentalMaterialApi::class)

package com.example.taskapp.screens.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.taskapp.models.Priority
import com.example.taskapp.models.Task
import com.example.taskapp.ui.theme.HighPriorityColor
import com.example.taskapp.ui.theme.LARGE_PADDING
import com.example.taskapp.utils.Action
import com.example.taskapp.utils.RequestState
import com.example.taskapp.utils.SearchAppBarState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TaskLists(
    allTasks: RequestState<List<Task>>,
    searchedTasks: RequestState<List<Task>>,
    lowPriorityTasks: RequestState<List<Task>>,
    highPriorityTasks: RequestState<List<Task>>,
    sortState: Priority,
    searchAppBarState: SearchAppBarState,
    searchedText: String,
    onSwipeToDelete: (Action, Task) -> Unit,
    onCheckboxClicked: (Action, Task) -> Unit,
    onTaskSelect: (Int) -> Unit
) {

    if (searchAppBarState == SearchAppBarState.TRIGGRED) {
        ManageTaskList(
            tasks = searchedTasks,
            searchedText = searchedText,
            onSwipeToDelete = onSwipeToDelete,
            onCheckboxClicked = onCheckboxClicked,
            onTaskSelect = onTaskSelect
        )
    } else {
        when(sortState) {
            Priority.HIGH -> ManageTaskList(
                tasks = highPriorityTasks,
                searchedText = searchedText,
                onSwipeToDelete = onSwipeToDelete,
                onCheckboxClicked = onCheckboxClicked,
                onTaskSelect = onTaskSelect
            )
            Priority.LOW -> ManageTaskList(
                tasks = lowPriorityTasks,
                searchedText = searchedText,
                onSwipeToDelete = onSwipeToDelete,
                onCheckboxClicked = onCheckboxClicked,
                onTaskSelect = onTaskSelect
            )
            else -> ManageTaskList(
                tasks = allTasks,
                searchedText = searchedText,
                onSwipeToDelete = onSwipeToDelete,
                onCheckboxClicked = onCheckboxClicked,
                onTaskSelect = onTaskSelect
            )
        }
    }
}

@Composable
fun ManageTaskList(
    tasks: RequestState<List<Task>>,
    searchedText: String,
    onSwipeToDelete: (Action, Task) -> Unit,
    onCheckboxClicked: (Action, Task) -> Unit,
    onTaskSelect: (Int) -> Unit
) {
    when(tasks) {
        is RequestState.Loading -> CircularProgressIndicator()

        is RequestState.Success -> {
            if (tasks.data.isEmpty())
                EmptyContent()
            else
                DisplayTasks(
                    tasks = tasks.data,
                    searchedText = searchedText,
                    onSwipeToDelete = onSwipeToDelete,
                    onCheckboxClicked = onCheckboxClicked,
                    onTaskSelect = onTaskSelect
                )
        }

        is RequestState.Error -> {}

        is RequestState.Idle -> {}
    }
}

@Composable
fun DisplayTasks(
    tasks: List<Task>,
    searchedText: String,
    onSwipeToDelete: (Action, Task) -> Unit,
    onCheckboxClicked: (Action, Task) -> Unit,
    onTaskSelect: (Int) -> Unit
) {
    LazyColumn {
        items(
            items = if (searchedText == "") tasks
            else tasks.filter {filteredTask ->
                filteredTask.title.contains(searchedText, ignoreCase = true)
            },
            key = {
                it.id
            }
        ) { task ->
            val dismissState = rememberDismissState()
            val dismissDirection = dismissState.dismissDirection
            val isDismissed = dismissState.isDismissed(DismissDirection.EndToStart)
            if (isDismissed && dismissDirection == DismissDirection.EndToStart) {
                val scope = rememberCoroutineScope()
                scope.launch {
                    delay(500L)
                    onSwipeToDelete(Action.DELETE, task)
                }
            }
            val degrees by animateFloatAsState(targetValue =
            if (dismissState.targetValue == DismissValue.Default) 0f
                else -45f
            )

            var itemAppeared by remember { mutableStateOf(false) }
            LaunchedEffect(key1 = true) {
                itemAppeared = true
            }

            AnimatedVisibility(
                visible = itemAppeared && !isDismissed,
                enter = expandVertically(
                    animationSpec = tween(
                        durationMillis = 500
                    )
                ),
                exit = shrinkVertically(
                    animationSpec = tween(
                        durationMillis = 500
                    )
                )
            ) {
                SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(DismissDirection.EndToStart),
                    dismissThresholds = { FractionalThreshold(fraction = 0.3f) },
                    background = { RedBackgroundSwipeDelete(degrees = degrees) },
                    dismissContent = {
                        TaskItem(
                            task = task,
                            onCheckboxItemClicked = { isChecked ->
                                onCheckboxClicked(Action.UPDATE, task.copy(isCompleted = isChecked))
                            },
                            onTaskSelect = onTaskSelect
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun RedBackgroundSwipeDelete(degrees: Float) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HighPriorityColor)
            .padding(horizontal = LARGE_PADDING),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            modifier = Modifier.rotate(degrees),
            imageVector = Icons.Filled.Delete,
            contentDescription = null,
            tint = Color.White
        )
    }
}