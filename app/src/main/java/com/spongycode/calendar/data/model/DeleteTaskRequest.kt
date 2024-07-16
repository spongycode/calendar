package com.spongycode.calendar.data.model

data class DeleteTaskRequest(
    val user_id: Int,
    val task_id: Int
)