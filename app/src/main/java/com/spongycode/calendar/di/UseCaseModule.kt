package com.spongycode.calendar.di

import com.spongycode.calendar.domain.repository.TaskRepository
import com.spongycode.calendar.domain.usecase.AddTaskToCloudUseCase
import com.spongycode.calendar.domain.usecase.AddTaskToLocalUseCase
import com.spongycode.calendar.domain.usecase.DeleteTaskFromCloudUseCase
import com.spongycode.calendar.domain.usecase.DeleteTaskFromLocalUseCase
import com.spongycode.calendar.domain.usecase.LoadTasksForCurrentMonthUseCase
import com.spongycode.calendar.domain.usecase.LoadTasksFromCloudUseCase
import com.spongycode.calendar.domain.usecase.LoadTasksFromLocalUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideLoadTasksForCurrentMonthUseCase(taskRepository: TaskRepository): LoadTasksForCurrentMonthUseCase {
        return LoadTasksForCurrentMonthUseCase(taskRepository)
    }

    @Provides
    fun provideAddTaskToLocalUseCase(taskRepository: TaskRepository): AddTaskToLocalUseCase {
        return AddTaskToLocalUseCase(taskRepository)
    }

    @Provides
    fun provideAddTaskToCloudUseCase(taskRepository: TaskRepository): AddTaskToCloudUseCase {
        return AddTaskToCloudUseCase(taskRepository)
    }

    @Provides
    fun provideDeleteTaskFromLocalUseCase(taskRepository: TaskRepository): DeleteTaskFromLocalUseCase {
        return DeleteTaskFromLocalUseCase(taskRepository)
    }

    @Provides
    fun provideDeleteTaskFromCloudUseCase(taskRepository: TaskRepository): DeleteTaskFromCloudUseCase {
        return DeleteTaskFromCloudUseCase(taskRepository)
    }

    @Provides
    fun provideLoadTasksFromLocalUseCase(taskRepository: TaskRepository): LoadTasksFromLocalUseCase {
        return LoadTasksFromLocalUseCase(taskRepository)
    }

    @Provides
    fun provideLoadTasksFromCloudUseCase(taskRepository: TaskRepository): LoadTasksFromCloudUseCase {
        return LoadTasksFromCloudUseCase(taskRepository)
    }
}