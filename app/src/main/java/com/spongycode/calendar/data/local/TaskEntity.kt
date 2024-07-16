package com.spongycode.calendar.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.spongycode.calendar.domain.model.Task

@Entity
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val year: Int,
    val month: Int,
    val day: Int,
    val timestamp: String
) {
    fun toTask(): Task = Task(
        title = title,
        description = description,
        year = year,
        month = month,
        day = day,
        timestamp = timestamp
    )
}