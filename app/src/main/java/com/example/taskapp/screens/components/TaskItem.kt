@file:OptIn(ExperimentalMaterialApi::class)

package com.example.taskapp.screens.components

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.taskapp.models.Task
import com.example.taskapp.ui.theme.MEDIUM_PADDING
import com.example.taskapp.ui.theme.PRIORITY_INDICATOR_SIZE
import com.example.taskapp.ui.theme.itemBackgroundColor
import com.example.taskapp.ui.theme.itemTextColor
import com.example.taskapp.utils.formatDate
import androidx.compose.material3.MaterialTheme as MaterialTheme3

@Composable
fun TaskItem(
    task: Task,
    onCheckboxItemClicked: (Boolean) -> Unit,
    onTaskSelect: (Int) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                ambientColor = MaterialTheme3.colorScheme.itemTextColor
            ),
        color = MaterialTheme3.colorScheme.itemBackgroundColor,
        shape = RectangleShape,
        onClick = {
            onTaskSelect(task.id)
        }
    ) {
        Column(
            modifier = Modifier
                .padding(MEDIUM_PADDING)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Box {
                    Canvas(modifier = Modifier.size(PRIORITY_INDICATOR_SIZE)) {
                        drawCircle(color = task.priority.color)
                    }
                }
                Text(
                    modifier = Modifier
                        .weight(8f)
                        .padding(start = 10.dp),
                    text = task.title,
                    color = MaterialTheme3.colorScheme.itemTextColor,
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
                Checkbox(
                    modifier = Modifier.weight(1f),
                    checked = task.isCompleted,
                    colors = CheckboxDefaults.colors(
                        uncheckedColor = MaterialTheme3.colorScheme.itemTextColor
                    ),
                    onCheckedChange = {
                        Log.d("DebugTaskApp", "item checked: $it")
                        onCheckboxItemClicked(it)
                    }
                )
            }
            Text(
                modifier = Modifier.padding(horizontal = 26.dp),
                text = task.description,
                color = MaterialTheme3.colorScheme.itemTextColor,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.padding(horizontal = 26.dp),
                text = formatDate(task.entryDate.time),
                color = MaterialTheme3.colorScheme.itemTextColor,
                style = MaterialTheme.typography.caption
            )
        }
    }
}