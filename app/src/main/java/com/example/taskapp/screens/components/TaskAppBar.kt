package com.example.taskapp.screens.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.taskapp.R
import com.example.taskapp.models.Task
import com.example.taskapp.ui.theme.topAppBarBackgroundColor
import com.example.taskapp.ui.theme.topAppBarContentColor
import com.example.taskapp.utils.Action
import androidx.compose.material3.MaterialTheme as MaterialTheme3

@Composable
fun TaskAppBar(
    selectedTask: Task?,
    goToListScreen: (Action) -> Unit
) {
    if (selectedTask == null)
        NewTaskAppBar(goToListScreen = goToListScreen)
    else
        ExistingTaskAppBar(selectedTask = selectedTask, goToListScreen = goToListScreen)
}


@Composable
fun NewTaskAppBar(
    goToListScreen: (Action) -> Unit
) {
    TopAppBar(
        navigationIcon = {
            BackAction(onBackClicked = goToListScreen)
        },
        title = {
            Text(text = "Add Task", color = MaterialTheme3.colorScheme.topAppBarContentColor)
        },
        actions = {
            AddAction(onAddClicked = goToListScreen)
        },
        backgroundColor = MaterialTheme3.colorScheme.topAppBarBackgroundColor
    )
}


@Composable
fun ExistingTaskAppBar(
    selectedTask: Task,
    goToListScreen: (Action) -> Unit
) {
    TopAppBar(
        navigationIcon = {
            CloseAction(onCloseClicked = goToListScreen)
        },
        title = {
            Text(text = selectedTask.title,
                color = MaterialTheme3.colorScheme.topAppBarContentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis)
        },
        actions = {
            DeleteAction(selectedTask = selectedTask, onDeleteClicked = goToListScreen)
            UpdateAction(onUpdateClicked = goToListScreen)
        },
        backgroundColor = MaterialTheme3.colorScheme.topAppBarBackgroundColor
    )
}


@Composable
fun BackAction(
    onBackClicked: (Action) -> Unit
) {
    IconButton(onClick = { onBackClicked(Action.CLOSE) }) {
        Icon(imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Back Arrow",
            tint = MaterialTheme3.colorScheme.topAppBarContentColor)
    }
}


@Composable
fun AddAction(
    onAddClicked: (Action) -> Unit
) {
    IconButton(onClick = { onAddClicked(Action.ADD) }) {
        Icon(imageVector = Icons.Filled.Check,
            contentDescription = "Add icon",
            tint = MaterialTheme3.colorScheme.topAppBarContentColor)
    }
}


@Composable
fun CloseAction(
    onCloseClicked: (Action) -> Unit
) {
    IconButton(onClick = { onCloseClicked(Action.CLOSE) }) {
        Icon(imageVector = Icons.Filled.Close,
            contentDescription = "Close icon",
            tint = MaterialTheme3.colorScheme.topAppBarContentColor)
    }
}


@Composable
fun UpdateAction(
    onUpdateClicked: (Action) -> Unit
) {
    IconButton(onClick = { onUpdateClicked(Action.UPDATE) }) {
        Icon(imageVector = Icons.Filled.Check,
            contentDescription = "Update icon",
            tint = MaterialTheme3.colorScheme.topAppBarContentColor)
    }
}


@Composable
fun DeleteAction(
    selectedTask: Task,
    onDeleteClicked: (Action) -> Unit
) {
    var openDialog by remember { mutableStateOf(false) }

    IconButton(onClick = { openDialog = true }) {
        Icon(imageVector = Icons.Filled.Delete,
            contentDescription = "Delete icon",
            tint = MaterialTheme3.colorScheme.topAppBarContentColor)
    }

    DisplayAlertDialog(
        title = stringResource(id = R.string.delete_task, selectedTask.title),
        message = stringResource(id = R.string.delete_task_confirmation, selectedTask.title),
        openDialog = openDialog,
        closeDialog = { openDialog = false },
        onYesClicked = { onDeleteClicked(Action.DELETE) }
    )
}
