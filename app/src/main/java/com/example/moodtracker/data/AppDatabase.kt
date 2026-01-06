package com.example.moodtracker.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MoodEntryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun moodDao(): MoodDao
}