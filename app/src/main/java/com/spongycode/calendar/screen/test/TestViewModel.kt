package com.spongycode.calendar.screen.test

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spongycode.calendar.domain.model.Task
import com.spongycode.calendar.domain.repository.TaskRepository
import com.spongycode.calendar.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {
    fun getAllTasksFromCloud() {
        viewModelScope.launch {
            repository.getAllTasksFromCloud().collect { resource ->
                when (resource) {
                    is Resource.Progress -> Log.d("TaskViewModel", "Loading tasks from cloud...")
                    is Resource.Success -> Log.d(
                        "TaskViewModel",
                        "Tasks from cloud: ${resource.data}"
                    )

                    is Resource.Error -> Log.e("TaskViewModel", "Error: ${resource.message}")
                }
            }
        }
    }

    fun storeTaskOnCloud() {
        val task = Task(
            title = "Title ${System.currentTimeMillis()}",
            description = "Description ${System.currentTimeMillis()}",
            year = 2024,
            month = 7,
            day = 17,
            timestamp = System.currentTimeMillis().toString()
        )

        viewModelScope.launch {
            repository.storeTaskOnCloud(task).collect { resource ->
                when (resource) {
                    is Resource.Progress -> Log.d("TaskViewModel", "Storing task on cloud...")
                    is Resource.Success -> Log.d("TaskViewModel", "Task stored: ${resource.data}")
                    is Resource.Error -> Log.e("TaskViewModel", "Error: ${resource.message}")
                }
            }
        }
    }

    fun deleteTaskFromCloud(timestamp: String) {
        viewModelScope.launch {
            repository.deleteTaskFromCloud(timestamp).collect { resource ->
                when (resource) {
                    is Resource.Progress -> Log.d("TaskViewModel", "Deleting task from cloud...")
                    is Resource.Success -> Log.d("TaskViewModel", "Task deleted: ${resource.data}")
                    is Resource.Error -> Log.e("TaskViewModel", "Error: ${resource.message}")
                }
            }
        }
    }

    fun getAllTasksFromLocal() {
        viewModelScope.launch {
            repository.getAllTasksFromLocal().collect { resource ->
                when (resource) {
                    is Resource.Progress -> Log.d("TaskViewModel", "Loading tasks from local...")
                    is Resource.Success -> Log.d(
                        "TaskViewModel",
                        "Tasks from local: ${resource.data}"
                    )

                    is Resource.Error -> Log.e("TaskViewModel", "Error: ${resource.message}")
                }
            }
        }
    }

    fun storeTaskOnLocal() {
        val task = Task(
            title = "Local Title ${System.currentTimeMillis()}",
            description = "Local Description ${System.currentTimeMillis()}",
            year = 2024,
            month = 7,
            day = 17,
            timestamp = System.currentTimeMillis().toString()
        )

        viewModelScope.launch {
            repository.storeTaskOnLocal(task).collect { resource ->
                when (resource) {
                    is Resource.Progress -> Log.d("TaskViewModel", "Storing task locally...")
                    is Resource.Success -> Log.d(
                        "TaskViewModel",
                        "Local task stored: ${resource.data}"
                    )

                    is Resource.Error -> Log.e("TaskViewModel", "Error: ${resource.message}")
                }
            }
        }
    }

    fun deleteTaskFromLocal(timestamp: String) {
        viewModelScope.launch {
            repository.deleteTaskFromLocal(timestamp).collect { resource ->
                when (resource) {
                    is Resource.Progress -> Log.d("TaskViewModel", "Deleting local task...")
                    is Resource.Success -> Log.d(
                        "TaskViewModel",
                        "Local task deleted: ${resource.data}"
                    )

                    is Resource.Error -> Log.e("TaskViewModel", "Error: ${resource.message}")
                }
            }
        }
    }

    fun getTasksByMonthFromLocal(year: Int, month: Int) {
        viewModelScope.launch {
            repository.getTasksByMonthFromLocal(year, month).collect { resource ->
                when (resource) {
                    is Resource.Progress -> Log.d(
                        "TaskViewModel",
                        "Loading tasks by month from local..."
                    )

                    is Resource.Success -> Log.d(
                        "TaskViewModel",
                        "Tasks by month: ${resource.data}"
                    )

                    is Resource.Error -> Log.e("TaskViewModel", "Error: ${resource.message}")
                }
            }
        }
    }

    fun getTasksByDayFromLocal(year: Int, month: Int, day: Int) {
        viewModelScope.launch {
            repository.getTasksByDayFromLocal(year, month, day).collect { resource ->
                when (resource) {
                    is Resource.Progress -> Log.d(
                        "TaskViewModel",
                        "Loading tasks by day from local..."
                    )

                    is Resource.Success -> Log.d("TaskViewModel", "Tasks by day: ${resource.data}")
                    is Resource.Error -> Log.e("TaskViewModel", "Error: ${resource.message}")
                }
            }
        }
    }
}