package com.spongycode.calendar.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.spongycode.calendar.data.local.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideWeatherInfoDatabase(app: Application): TaskDatabase {
        return Room.databaseBuilder(
            app, TaskDatabase::class.java, "task_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("task_prefs", Context.MODE_PRIVATE)
    }
}