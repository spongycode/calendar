package com.spongycode.calendar.data.model

data class TaskDetailDto(
    val task_id: Int,
    val task_detail: TaskDto
) {
    fun toTaskDto(): TaskDto = TaskDto(
        title = task_detail.title,
        description = task_detail.description
    )
}