package com.example.taskapp.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.taskapp.models.Priority
import com.example.taskapp.ui.theme.LARGE_PADDING
import com.example.taskapp.ui.theme.MEDIUM_PADDING
import com.example.taskapp.ui.theme.appBackgroundColor
import com.example.taskapp.ui.theme.taskContentColor
import com.example.taskapp.utils.Constants
import androidx.compose.material3.MaterialTheme as MaterialTheme3

@Composable
fun TaskContent(
    title: String,
    description: String,
    priority: Priority,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPriorityChange: (Priority) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme3.colorScheme.appBackgroundColor)
            .padding(all = LARGE_PADDING)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = title,
            onValueChange = {
                onTitleChange(it)
            },
            label = { Text(text = "Title", color = MaterialTheme3.colorScheme.taskContentColor) },
            textStyle = MaterialTheme.typography.body1,
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme3.colorScheme.taskContentColor,
                cursorColor = MaterialTheme3.colorScheme.taskContentColor,
                unfocusedIndicatorColor = MaterialTheme3.colorScheme.taskContentColor,
                backgroundColor = MaterialTheme3.colorScheme.appBackgroundColor
            ),
            isError = title.length > Constants.MAX_TITLE_LENGTH
        )
        Divider(
            modifier = Modifier.height(MEDIUM_PADDING),
            color = MaterialTheme3.colorScheme.appBackgroundColor
        )
        PriorityDropDown(
            priority = priority,
            onPrioritySelected = onPriorityChange
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxSize(),
            value = description,
            onValueChange ={
                onDescriptionChange(it)
            },
            label = { Text(text = "Description", color = MaterialTheme3.colorScheme.taskContentColor) },
            textStyle = MaterialTheme.typography.body1,
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme3.colorScheme.taskContentColor,
                cursorColor = MaterialTheme3.colorScheme.taskContentColor,
                unfocusedIndicatorColor = MaterialTheme3.colorScheme.taskContentColor,
                backgroundColor = MaterialTheme3.colorScheme.appBackgroundColor
            )
        )
    }
}