package com.example.ereport.model

import androidx.room.TypeConverter

class RoomConverters {

    @TypeConverter
    fun fromStatus(status: ReportStatus): String = status.name

    @TypeConverter
    fun toStatus(value: String): ReportStatus = ReportStatus.valueOf(value)
}