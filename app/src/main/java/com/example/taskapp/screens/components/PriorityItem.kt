package com.example.taskapp.screens.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.taskapp.models.Priority
import com.example.taskapp.ui.theme.MEDIUM_PADDING
import com.example.taskapp.ui.theme.PRIORITY_INDICATOR_SIZE
import com.example.taskapp.ui.theme.Typography

@Composable
fun PriorityItem(priority: Priority) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(modifier = Modifier.size(PRIORITY_INDICATOR_SIZE)) {
            drawCircle(color = priority.color)
        }
        Text(modifier = Modifier.padding(start = MEDIUM_PADDING),
            text = priority.name,
            style = Typography.bodySmall,
            color = MaterialTheme.colors.onSurface
        )
    }
}