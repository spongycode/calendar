package com.spongycode.calendar.domain.usecase

import com.spongycode.calendar.domain.model.Task
import com.spongycode.calendar.domain.repository.TaskRepository
import com.spongycode.calendar.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadTasksForCurrentMonthUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(year: Int, month: Int): Flow<Resource<List<Task>>> {
        return taskRepository.getTasksByMonthFromLocal(year, month)
    }
}