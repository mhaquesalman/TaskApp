package com.example.taskapp.utils

enum class Action {
    ADD,
    UPDATE,
    DELETE,
    DELETE_ALL,
    UNDO,
    CLOSE,
    NO_ACTION
}

fun String.toAction(): Action {
    return when {
        this == "ADD" -> Action.ADD
        this == "UPDATE" -> Action.UPDATE
        this == "DELETE" -> Action.DELETE
        this == "DELETE_ALL" -> Action.DELETE_ALL
        this == "UNDO" -> Action.UNDO
        else -> Action.CLOSE
    }
}