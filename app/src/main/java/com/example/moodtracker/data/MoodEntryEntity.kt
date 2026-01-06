package com.example.moodtracker.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "mood_entries",
    indices = [Index(value = ["dateIso"], unique = true)]
)
data class MoodEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateIso: String,          // yyyy-MM-dd
    val moodScore: Int,           // 0..4
    val tagsCsv: String,          // "Work,Sleep"
    val note: String?,            // optional
    val createdAtEpochMs: Long,
    val updatedAtEpochMs: Long
)
