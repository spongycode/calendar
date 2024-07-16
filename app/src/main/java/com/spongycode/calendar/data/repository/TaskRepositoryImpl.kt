package com.spongycode.calendar.data.repository

import com.spongycode.calendar.BuildConfig
import com.spongycode.calendar.data.local.TaskDao
import com.spongycode.calendar.data.model.DeleteTaskRequest
import com.spongycode.calendar.data.model.GetTasksRequest
import com.spongycode.calendar.data.model.StoreTaskRequest
import com.spongycode.calendar.data.remote.TaskApi
import com.spongycode.calendar.domain.model.Task
import com.spongycode.calendar.domain.repository.TaskRepository
import com.spongycode.calendar.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TaskRepositoryImpl(
    private val api: TaskApi,
    private val dao: TaskDao
) : TaskRepository {
    override suspend fun getAllTasksFromCloud(): Flow<Resource<List<Task>>> = flow {
        emit(Resource.Progress())
        try {
            val response =
                api.getTaskList(GetTasksRequest(user_id = BuildConfig.USER_ID.toInt()))
            val tasks = response.tasks.map { it.toTaskDto().toTaskEntity().toTask() }
            emit(Resource.Success(tasks))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error"))
        }
    }

    override suspend fun storeTaskOnCloud(task: Task): Flow<Resource<Boolean>> = flow {
        emit(Resource.Progress())
        try {
            val response = api.storeTask(StoreTaskRequest(user_id = BuildConfig.USER_ID.toInt(), task = task.toTaskDto()))
            emit(Resource.Success(response.status.lowercase() == "success"))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error", false))
        }
    }

    override suspend fun deleteTaskFromCloud(timestamp: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Progress())
        try {
            val taskEntity = dao.getAllTasks().find { it.timestamp == timestamp }
            taskEntity?.let {
                val response =
                    api.deleteTask(DeleteTaskRequest(user_id = BuildConfig.USER_ID.toInt(), task_id = it.id.toInt()))
                emit(Resource.Success(response.status.lowercase() == "success"))
            } ?: emit(Resource.Error("Task not found", false))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error", false))
        }
    }

    override suspend fun getAllTasksFromLocal(): Flow<Resource<List<Task>>> = flow {
        emit(Resource.Progress())
        try {
            val tasks = dao.getAllTasks().map { it.toTask() }
            emit(Resource.Success(tasks))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error"))
        }
    }

    override suspend fun storeTaskOnLocal(task: Task): Flow<Resource<Boolean>> = flow {
        emit(Resource.Progress())
        try {
            dao.insertTask(task.toTaskDto().toTaskEntity())
            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error", false))
        }
    }

    override suspend fun deleteTaskFromLocal(timestamp: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Progress())
        try {
            dao.deleteTask(timestamp)
            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error", false))
        }
    }

    override suspend fun getTasksByMonthFromLocal(
        year: Int,
        month: Int
    ): Flow<Resource<List<Task>>> = flow {
        emit(Resource.Progress())
        try {
            val tasks = dao.getTasksByMonth(year, month).map { it.toTask() }
            emit(Resource.Success(tasks))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error"))
        }
    }

    override suspend fun getTasksByDayFromLocal(
        year: Int,
        month: Int,
        day: Int
    ): Flow<Resource<List<Task>>> = flow {
        emit(Resource.Progress())
        try {
            val tasks = dao.getTasksByDay(year, month, day).map { it.toTask() }
            emit(Resource.Success(tasks))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error"))
        }
    }
}