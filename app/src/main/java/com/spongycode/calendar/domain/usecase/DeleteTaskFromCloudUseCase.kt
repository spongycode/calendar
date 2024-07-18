package com.spongycode.calendar.domain.usecase

import com.spongycode.calendar.domain.model.Task
import com.spongycode.calendar.domain.repository.TaskRepository
import com.spongycode.calendar.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteTaskFromCloudUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(task: Task): Flow<Resource<Boolean>> = flow {
        taskRepository.deleteTaskFromCloud(task.timestamp).collect { cloudResource ->
            emit(cloudResource)
        }
    }
}