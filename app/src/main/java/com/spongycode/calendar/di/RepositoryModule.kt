package com.spongycode.calendar.di

import com.spongycode.calendar.data.local.TaskDatabase
import com.spongycode.calendar.data.remote.TaskApi
import com.spongycode.calendar.data.repository.TaskRepositoryImpl
import com.spongycode.calendar.domain.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun bindTaskRepository(
        db: TaskDatabase,
        api: TaskApi
    ): TaskRepository {
        return TaskRepositoryImpl(api, db.dao)
    }
}