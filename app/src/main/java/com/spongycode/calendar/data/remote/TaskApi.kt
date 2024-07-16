package com.spongycode.calendar.data.remote

import com.spongycode.calendar.data.model.DeleteTaskRequest
import com.spongycode.calendar.data.model.GetTasksRequest
import com.spongycode.calendar.data.model.StoreTaskRequest
import com.spongycode.calendar.data.model.TaskListDto
import com.spongycode.calendar.data.model.TaskResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface TaskApi {
    @POST("api/storeCalendarTask")
    suspend fun storeTask(
        @Body request: StoreTaskRequest
    ): TaskResponseDto

    @POST("api/getCalendarTaskList")
    suspend fun getTaskList(
        @Body request: GetTasksRequest
    ): TaskListDto

    @POST("api/deleteCalendarTask")
    suspend fun deleteTask(
        @Body request: DeleteTaskRequest
    ): TaskResponseDto
}
