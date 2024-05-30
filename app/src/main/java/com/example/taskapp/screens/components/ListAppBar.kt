package com.example.taskapp.screens.components

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import com.example.taskapp.R
import com.example.taskapp.models.Priority
import com.example.taskapp.ui.theme.MEDIUM_PADDING
import com.example.taskapp.ui.theme.TOP_APPBAR_HEIGHT
import com.example.taskapp.ui.theme.Typography
import com.example.taskapp.ui.theme.topAppBarBackgroundColor
import com.example.taskapp.ui.theme.topAppBarContentColor
import com.example.taskapp.utils.Action
import com.example.taskapp.utils.SearchAppBarState
import com.example.taskapp.utils.TrailingIconState
import com.example.taskapp.viewmodels.SharedViewModel
import androidx.compose.material3.MaterialTheme as MaterialTheme3

@Composable
fun AppBar(
    sharedViewModel: SharedViewModel,
    searchAppBarState: SearchAppBarState,
    searchTextState: String,
    isDark: Boolean
    ) {
    when (searchAppBarState) {
        SearchAppBarState.CLOSED -> {
            DefaultAppBar(
                onSearchBarClicked = {
                    sharedViewModel.searchAppBarState.value = SearchAppBarState.OPENED
                },
                onSortClicked = {
                    sharedViewModel.createSortState(it)
                },
                onDeleteClicked = {
                    sharedViewModel.action.value = Action.DELETE_ALL
                },
                onThemeClicked = {
                    Log.d("DebugTaskApp", "ThemeApp: $it")
                    sharedViewModel.createThemeState(it)
                },
                isDark = isDark
            )
        }
        else -> {
            SearchAppBar(
                text = searchTextState,
                onTextChange = { newValue ->
                    sharedViewModel.searchTextState.value = newValue
                },
                onCloseClicked = {
                    sharedViewModel.searchAppBarState.value = SearchAppBarState.CLOSED
                    sharedViewModel.searchTextState.value = ""
                },
                onSearchIconClicked = {
                    sharedViewModel.searchDatabase()
                }
            )
        }
    }
}

@Composable
fun DefaultAppBar(
    onSearchBarClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteClicked: () -> Unit,
    onThemeClicked: (Boolean) -> Unit,
    isDark: Boolean
) {
    TopAppBar(title = {
        Text(text = "Task App",
            style = MaterialTheme3.typography.headlineLarge,
            color = MaterialTheme3.colorScheme.topAppBarContentColor)
    },
        actions = {
            AppBarActions(
                onSearchBarClicked = onSearchBarClicked,
                onSortClicked = onSortClicked,
                onDeleteClicked = onDeleteClicked,
                onThemeClicked = onThemeClicked,
                isDark = isDark
            )
        },
        backgroundColor = MaterialTheme3.colorScheme.topAppBarBackgroundColor
    )
}

@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchIconClicked: () -> Unit,
) {
    var trailingIconState by remember {
        mutableStateOf(TrailingIconState.READY_TO_DELETE)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(TOP_APPBAR_HEIGHT),
        elevation = AppBarDefaults.BottomAppBarElevation,
        color = MaterialTheme3.colorScheme.topAppBarBackgroundColor
    ) {
        TextField(
            value = text,
            onValueChange = { newValue ->
                onTextChange(newValue)
            }, placeholder = {
                Text( modifier = Modifier.alpha(ContentAlpha.medium),
                    text = "Search",
                    color = Color.White)
            },
            textStyle = TextStyle(
                color = MaterialTheme3.colorScheme.topAppBarContentColor,
                fontSize = MaterialTheme.typography.subtitle1.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(modifier = Modifier.alpha(ContentAlpha.disabled),
                    onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Filled.Search,
                        contentDescription = "Search Icon",
                        tint = MaterialTheme3.colorScheme.topAppBarContentColor)
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        when (trailingIconState) {
                            TrailingIconState.READY_TO_DELETE -> {
                                if (text.isNotEmpty())
                                    onTextChange("")
                                else
                                    onCloseClicked()
                                trailingIconState = TrailingIconState.READY_TO_CLOSE
                            }
                            TrailingIconState.READY_TO_CLOSE -> {
                                if (text.isNotEmpty()) {
                                    onTextChange("")
                                } else {
                                    onCloseClicked()
                                    trailingIconState = TrailingIconState.READY_TO_DELETE
                                }
                            }
                        }
                    }) {
                    Icon(imageVector = Icons.Filled.Close,
                        contentDescription = "Search Icon",
                        tint = MaterialTheme3.colorScheme.topAppBarContentColor)
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchIconClicked()
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = MaterialTheme3.colorScheme.topAppBarContentColor,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = Color.Transparent
            )
        )
    }
}

@Composable
fun AppBarActions(
    onSearchBarClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteClicked: () -> Unit,
    onThemeClicked: (Boolean) -> Unit,
    isDark: Boolean
) {
    SearchAction(onSearchBarClicked = onSearchBarClicked)
    SortAction(onSortClicked = onSortClicked)
    DeleteAllAction(
        onDeleteClicked = onDeleteClicked,
        onThemeClicked = onThemeClicked,
        isDark = isDark
    )
}

@Composable
fun SearchAction(
    onSearchBarClicked: () -> Unit
) {
    IconButton(onClick = { onSearchBarClicked() }) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = "Search Tasks",
            tint = MaterialTheme3.colorScheme.topAppBarContentColor)

    }
}

@Composable
fun SortAction(
    onSortClicked: (Priority) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }

    IconButton(onClick = { expanded.value = true }) {
        Icon(painter = painterResource(id = R.drawable.ic_filter_list),
            contentDescription = "Sort Priority",
            tint = MaterialTheme3.colorScheme.topAppBarContentColor
        )
        DropdownMenu(expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            DropdownMenuItem(onClick = {
                expanded.value = false
                onSortClicked(Priority.HIGH)
            }) {
                PriorityItem(priority = Priority.HIGH)
            }
            DropdownMenuItem(onClick = {
                expanded.value = false
                onSortClicked(Priority.LOW)
            }) {
                PriorityItem(priority = Priority.LOW)
            }
            DropdownMenuItem(onClick = {
                expanded.value = false
                onSortClicked(Priority.NONE)
            }) {
                PriorityItem(priority = Priority.NONE)
            }
        }
    }
}

@Composable
fun DeleteAllAction(
    onDeleteClicked: () -> Unit,
    onThemeClicked: (Boolean) -> Unit,
    isDark: Boolean
) {
    var expanded by remember { mutableStateOf(false) }
    var openDialog by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(painter = painterResource(id = R.drawable.ic_more),
            contentDescription = "Sort Priority",
            tint = MaterialTheme3.colorScheme.topAppBarContentColor
        )
        DropdownMenu(expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(onClick = {
                openDialog = true
            }) {
                Text(modifier = Modifier.padding(MEDIUM_PADDING),
                    text = "Delete All",
                    style = Typography.bodyMedium)
            }
            Divider()
            DropdownMenuItem(onClick = {
                onThemeClicked(!isDark)
                expanded = false
            }) {
                Text(modifier = Modifier.padding(MEDIUM_PADDING),
                    text = if (isDark) "Light" else "Dark",
                    style = Typography.bodyMedium
                )
            }
        }
    }

    DisplayAlertDialog(
        title = "Remove All",
        message = "Do you want to remove all your tasks?",
        openDialog = openDialog,
        closeDialog = { openDialog = false },
        onYesClicked = {
            onDeleteClicked()
            expanded = false
        }
    )
}