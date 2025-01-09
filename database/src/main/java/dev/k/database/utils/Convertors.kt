package dev.k.database.utils

import androidx.room.TypeConverter
import java.text.DateFormat
import java.util.Date

internal class Convertors {

    @TypeConverter
    fun fromTimestamp(value: String?): Date? {
        return value?.let {
            DateFormat.getDateInstance().parse(it)
        }
    }


    @TypeConverter
    fun dateTimestamp(date: Date?): String? {
        return date?.time?.let {
            DateFormat.getDateTimeInstance().format(it)
        }
    }
}