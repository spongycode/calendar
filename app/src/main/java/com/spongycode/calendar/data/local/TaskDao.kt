package com.spongycode.calendar.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Query("SELECT * FROM TaskEntity WHERE year = :year AND month = :month AND day = :day")
    suspend fun getTasksByDay(year: Int, month: Int, day: Int): List<TaskEntity>

    @Query("SELECT * FROM TaskEntity WHERE year = :year AND month = :month")
    suspend fun getTasksByMonth(year: Int, month: Int): List<TaskEntity>

    @Query("SELECT * FROM TaskEntity")
    suspend fun getAllTasks(): List<TaskEntity>

    @Query("DELETE FROM TaskEntity WHERE timestamp = :timestamp")
    suspend fun deleteTask(timestamp: String)
}