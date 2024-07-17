package com.spongycode.calendar.screen.task

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spongycode.calendar.domain.model.Task
import com.spongycode.calendar.domain.repository.TaskRepository
import com.spongycode.calendar.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    init {
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            taskRepository.getAllTasksFromCloud().collect { resource ->
                when (resource) {
                    is Resource.Success -> _tasks.value = resource.data ?: emptyList()
                    is Resource.Error -> Log.e(
                        "TaskViewModel",
                        "Error loading tasks: ${resource.message}"
                    )

                    is Resource.Progress -> {
                    }
                }
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTaskFromLocal(task.timestamp).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        loadTasksForCurrentMonth(task.year, task.month)

                        taskRepository.deleteTaskFromCloud(task.timestamp)
                            .collect { cloudResource ->
                                when (cloudResource) {
                                    is Resource.Success -> {
                                        if (cloudResource.data != true) {
                                            Log.e(
                                                "TaskViewModel",
                                                "Failed to delete task from cloud"
                                            )
                                        }
                                    }

                                    is Resource.Error -> {
                                        Log.e(
                                            "TaskViewModel",
                                            "Error deleting task from cloud: ${cloudResource.message}"
                                        )
                                    }

                                    is Resource.Progress -> {
                                    }
                                }
                            }
                    }

                    is Resource.Error -> Log.e(
                        "TaskViewModel",
                        "Error deleting task: ${resource.message}"
                    )

                    is Resource.Progress -> {
                    }
                }
            }
        }
    }

    private fun loadTasksForCurrentMonth(
        year: Int = LocalDate.now().year,
        month: Int = LocalDate.now().monthValue
    ) {
        viewModelScope.launch {
            taskRepository.getTasksByMonthFromLocal(year, month).collect { resource ->
                when (resource) {
                    is Resource.Success -> _tasks.value = resource.data ?: emptyList()
                    is Resource.Error -> Log.e(
                        "TaskViewModel",
                        "Error loading tasks: ${resource.message}"
                    )

                    is Resource.Progress -> {
                    }
                }
            }
        }
    }
}