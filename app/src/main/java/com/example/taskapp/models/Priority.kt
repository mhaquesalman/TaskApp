package com.example.taskapp.models

import androidx.compose.ui.graphics.Color
import com.example.taskapp.ui.theme.HighPriorityColor
import com.example.taskapp.ui.theme.LowPriorityColor
import com.example.taskapp.ui.theme.MediumPriorityColor
import com.example.taskapp.ui.theme.NoPriorityColor

enum class Priority(val color: Color) {
    HIGH(HighPriorityColor),
    MEDIUM(MediumPriorityColor),
    LOW(LowPriorityColor),
    NONE(NoPriorityColor)
}