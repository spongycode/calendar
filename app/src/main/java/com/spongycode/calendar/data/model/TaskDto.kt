package com.spongycode.calendar.data.model

import com.spongycode.calendar.data.local.TaskEntity

data class TaskDto(
    val title: String,
    val description: String
) {
    fun toTaskEntity(): TaskEntity {
        val dollarIndex = title.lastIndexOf('$')
        var year = 0
        var month = 0
        var day = 0
        var currentTimeMillis = ""

        if (dollarIndex != -1) {
            val encodedPart = title.substring(dollarIndex + 1)
            val parts = encodedPart.split(':')
            if (parts.size == 4) {
                year = parts[0].toInt()
                month = parts[1].toInt()
                day = parts[2].toInt()
                currentTimeMillis = parts[3]
            }
        }

        return TaskEntity(
            title = title,
            description = description,
            year = year,
            month = month,
            day = day,
            timestamp = currentTimeMillis
        )
    }
}