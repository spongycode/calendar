package com.spongycode.calendar.screen.task

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spongycode.calendar.domain.model.Task
import com.spongycode.calendar.domain.usecase.DeleteTaskFromCloudUseCase
import com.spongycode.calendar.domain.usecase.DeleteTaskFromLocalUseCase
import com.spongycode.calendar.domain.usecase.LoadTasksFromCloudUseCase
import com.spongycode.calendar.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val getTasksFromCloudUseCase: LoadTasksFromCloudUseCase,
    private val deleteTaskFromLocalUseCase: DeleteTaskFromLocalUseCase,
    private val deleteTaskFromCloudUseCase: DeleteTaskFromCloudUseCase,
) : ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    init {
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            getTasksFromCloudUseCase().collect { resource ->
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
            deleteTaskFromLocalUseCase(task).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _tasks.value = _tasks.value.filter { it.timestamp != task.timestamp }
                        deleteTaskFromCloudUseCase(task).collect {}
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
}