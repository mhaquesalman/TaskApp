package com.example.taskapp.screens.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.ContentAlpha
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.example.taskapp.models.Priority
import com.example.taskapp.ui.theme.PRIORITY_DROPDOWN_HEIGHT
import com.example.taskapp.ui.theme.PRIORITY_INDICATOR_SIZE
import com.example.taskapp.ui.theme.appBackgroundColor
import com.example.taskapp.ui.theme.itemTextColor
import androidx.compose.material3.MaterialTheme as MaterialTheme3

@Composable
fun PriorityDropDown(
    priority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    var expanded: Boolean by remember {
        mutableStateOf(false)
    }

    val angle: Float by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme3.colorScheme.appBackgroundColor)
            .height(PRIORITY_DROPDOWN_HEIGHT)
            .clickable { expanded = true }
            .border(
                width = 1.dp,
                color = MaterialTheme3.colorScheme.itemTextColor,
                shape = MaterialTheme3.shapes.small
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(modifier = Modifier
            .size(PRIORITY_INDICATOR_SIZE)
            .weight(1f)) {
            drawCircle(color = priority.color)
        }
        Text(
            modifier = Modifier.weight(8f),
            text = priority.name,
            style = MaterialTheme.typography.subtitle2,
            color = MaterialTheme3.colorScheme.itemTextColor
        )
        IconButton(
            modifier = Modifier
                .weight(1.5f)
                .alpha(ContentAlpha.medium)
                .rotate(angle),
            onClick = { expanded = true }
        ) {
            Icon(imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Drop Arrow")
        }
        DropdownMenu(
            modifier = Modifier.fillMaxWidth(0.9f),
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            DropdownMenuItem(onClick = {
                onPrioritySelected(Priority.LOW)
                expanded = false
            }
            ) {
                PriorityItem(priority = Priority.LOW)
            }
            DropdownMenuItem(onClick = {
                onPrioritySelected(Priority.MEDIUM)
                expanded = false
            }
            ) {
                PriorityItem(priority = Priority.MEDIUM)
            }
            DropdownMenuItem(onClick = {
                onPrioritySelected(Priority.HIGH)
                expanded = false
            }
            ) {
                PriorityItem(priority = Priority.HIGH)
            }
        }
    }
}