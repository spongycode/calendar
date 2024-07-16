package com.spongycode.calendar.domain.repository

import com.spongycode.calendar.domain.model.Task
import com.spongycode.calendar.utils.Resource

import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun getAllTasksFromCloud(): Flow<Resource<List<Task>>>
    suspend fun storeTaskOnCloud(task: Task): Flow<Resource<Boolean>>
    suspend fun deleteTaskFromCloud(timestamp: String): Flow<Resource<Boolean>>

    suspend fun getAllTasksFromLocal(): Flow<Resource<List<Task>>>
    suspend fun storeTaskOnLocal(task: Task): Flow<Resource<Boolean>>
    suspend fun deleteTaskFromLocal(timestamp: String): Flow<Resource<Boolean>>

    suspend fun getTasksByMonthFromLocal(year: Int, month: Int): Flow<Resource<List<Task>>>
    suspend fun getTasksByDayFromLocal(year: Int, month: Int, day: Int): Flow<Resource<List<Task>>>
}