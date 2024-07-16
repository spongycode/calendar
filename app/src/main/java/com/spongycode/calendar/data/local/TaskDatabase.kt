package com.spongycode.calendar.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TaskEntity::class],
    version = 1
)

abstract class TaskDatabase : RoomDatabase() {
    abstract val dao: TaskDao
}