package com.spongycode.calendar.screen.calendar

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spongycode.calendar.domain.model.Task
import com.spongycode.calendar.domain.usecase.AddTaskToCloudUseCase
import com.spongycode.calendar.domain.usecase.AddTaskToLocalUseCase
import com.spongycode.calendar.domain.usecase.DeleteTaskFromCloudUseCase
import com.spongycode.calendar.domain.usecase.DeleteTaskFromLocalUseCase
import com.spongycode.calendar.domain.usecase.LoadTasksForCurrentMonthUseCase
import com.spongycode.calendar.domain.usecase.LoadTasksFromCloudUseCase
import com.spongycode.calendar.domain.usecase.LoadTasksFromLocalUseCase
import com.spongycode.calendar.utils.Constants.SHOULD_TRUST_CLOUD
import com.spongycode.calendar.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val loadTasksForCurrentMonthUseCase: LoadTasksForCurrentMonthUseCase,
    private val addTaskToLocalUseCase: AddTaskToLocalUseCase,
    private val addTaskToCloudUseCase: AddTaskToCloudUseCase,
    private val deleteTaskFromLocalUseCase: DeleteTaskFromLocalUseCase,
    private val deleteTaskFromCloudUseCase: DeleteTaskFromCloudUseCase,
    private val getTasksFromCloudUseCase: LoadTasksFromCloudUseCase,
    private val getTasksFromLocalUseCase: LoadTasksFromLocalUseCase,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _selectedDay = MutableStateFlow<Int?>(null)
    val selectedDay: StateFlow<Int?> = _selectedDay

    init {
        val shouldTrustCloud = sharedPreferences.getBoolean(SHOULD_TRUST_CLOUD, true)
        viewModelScope.launch {
            loadTasksForCurrentMonth()
            syncTasks(shouldTrustCloud = shouldTrustCloud)
        }
    }

    fun loadTasksForCurrentMonth(
        year: Int = LocalDate.now().year,
        month: Int = LocalDate.now().monthValue
    ) {
        viewModelScope.launch {
            loadTasksForCurrentMonthUseCase(year, month).collect { resource ->
                when (resource) {
                    is Resource.Success -> _tasks.value = resource.data ?: emptyList()
                    is Resource.Error -> Log.e(
                        "CalendarViewModel",
                        "Error loading tasks: ${resource.message}"
                    )

                    is Resource.Progress -> {}
                }
            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            addTaskToLocalUseCase(task).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        loadTasksForCurrentMonth(task.year, task.month)
                        addTaskToCloudUseCase(task).collect {}
                    }

                    is Resource.Error -> Log.e(
                        "CalendarViewModel",
                        "Error storing task: ${resource.message}"
                    )

                    is Resource.Progress -> {}
                }
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            deleteTaskFromLocalUseCase(task).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        loadTasksForCurrentMonth(task.year, task.month)
                        deleteTaskFromCloudUseCase(task).collect {}
                    }

                    is Resource.Error -> Log.e(
                        "CalendarViewModel",
                        "Error deleting task: ${resource.message}"
                    )

                    is Resource.Progress -> {}
                }
            }
        }
    }

    private fun syncTasks(shouldTrustCloud: Boolean) {
        viewModelScope.launch {
            if (shouldTrustCloud) {
                getTasksFromLocalUseCase().collect { tasksFromLocal ->
                    tasksFromLocal.data?.let { tasks ->
                        tasks.forEach { task ->
                            deleteTaskFromLocalUseCase(task).collect {}
                        }
                    }
                }

                getTasksFromCloudUseCase().collect { tasksFromCloud ->
                    tasksFromCloud.data?.let { tasks ->
                        tasks.forEach { task ->
                            addTaskToLocalUseCase(task).collect {}
                        }
                        sharedPreferences.edit().putBoolean(SHOULD_TRUST_CLOUD, false).apply()
                        loadTasksForCurrentMonth()
                    }
                }
            } else {
                getTasksFromCloudUseCase().collect { tasksFromCloud ->
                    tasksFromCloud.data?.let { tasks ->
                        tasks.forEach { task ->
                            deleteTaskFromCloudUseCase(task).collect {}
                        }
                    }
                }

                getTasksFromLocalUseCase().collect { tasksFromLocal ->
                    tasksFromLocal.data?.let { tasks ->
                        tasks.forEach { task ->
                            addTaskToCloudUseCase(task).collect {}
                        }
                        loadTasksForCurrentMonth()
                    }
                }
            }
        }
    }

    fun manualSync(onComplete: () -> Unit) {
        viewModelScope.launch {
            val shouldTrustCloud = sharedPreferences.getBoolean(SHOULD_TRUST_CLOUD, true)
            syncTasks(shouldTrustCloud = shouldTrustCloud)
            onComplete()
        }
    }

    fun selectDay(day: Int) {
        _selectedDay.value = day
    }
}