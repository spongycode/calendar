package com.spongycode.calendar.domain.model

import com.spongycode.calendar.data.model.TaskDto

data class Task(
    val title: String,
    val description: String,
    val year: Int,
    val month: Int,
    val day: Int,
    val timestamp: String
) {
    fun toTaskDto(): TaskDto = TaskDto(
        title = "$title$$year:$month:$day:$timestamp",
        description = description
    )
}
