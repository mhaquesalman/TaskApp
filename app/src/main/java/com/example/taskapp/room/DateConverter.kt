package com.example.taskapp.room

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {

    @TypeConverter
    fun ToTimestampFromDate(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun ToDateFromTimestamp(timestamp: Long): Date {
        return Date(timestamp)
    }
}