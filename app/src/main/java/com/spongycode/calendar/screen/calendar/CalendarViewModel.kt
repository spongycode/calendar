package com.spongycode.calendar.screen.calendar

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
class CalendarViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _selectedDay = MutableStateFlow<Int?>(null)
    val selectedDay: StateFlow<Int?> = _selectedDay

    init {
        loadTasksForCurrentMonth()
    }

    fun loadTasksForCurrentMonth(
        year: Int = LocalDate.now().year,
        month: Int = LocalDate.now().monthValue
    ) {
        viewModelScope.launch {
            taskRepository.getTasksByMonthFromLocal(year, month).collect { resource ->
                when (resource) {
                    is Resource.Success -> _tasks.value = resource.data ?: emptyList()
                    is Resource.Error -> Log.e(
                        "CalendarViewModel",
                        "Error loading tasks: ${resource.message}"
                    )

                    is Resource.Progress -> {
                    }
                }
            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            taskRepository.storeTaskOnLocal(task).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        loadTasksForCurrentMonth(task.year, task.month)
                        taskRepository.storeTaskOnCloud(task).collect { cloudResource ->
                            when (cloudResource) {
                                is Resource.Success -> {
                                    if (cloudResource.data != true) {
                                        Log.e("CalendarViewModel", "Failed to store task on cloud")
                                    }
                                }

                                is Resource.Error -> {
                                    Log.e(
                                        "CalendarViewModel",
                                        "Error storing task on cloud: ${cloudResource.message}"
                                    )
                                }

                                is Resource.Progress -> {
                                }
                            }
                        }
                    }

                    is Resource.Error -> Log.e(
                        "CalendarViewModel",
                        "Error storing task: ${resource.message}"
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
                                                "CalendarViewModel",
                                                "Failed to delete task from cloud"
                                            )
                                        }
                                    }

                                    is Resource.Error -> {
                                        Log.e(
                                            "CalendarViewModel",
                                            "Error deleting task from cloud: ${cloudResource.message}"
                                        )
                                    }

                                    is Resource.Progress -> {
                                    }
                                }
                            }
                    }

                    is Resource.Error -> Log.e(
                        "CalendarViewModel",
                        "Error deleting task: ${resource.message}"
                    )

                    is Resource.Progress -> {
                    }
                }
            }
        }
    }

    fun selectDay(day: Int) {
        _selectedDay.value = day
    }
}
