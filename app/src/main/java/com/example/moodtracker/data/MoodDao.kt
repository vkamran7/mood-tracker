package com.example.moodtracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {

    @Query("SELECT * FROM mood_entries WHERE dateIso = :dateIso LIMIT 1")
    suspend fun getByDate(dateIso: String): MoodEntryEntity?

    @Query("SELECT * FROM mood_entries ORDER BY dateIso DESC")
    fun observeAll(): Flow<List<MoodEntryEntity>>

    @Query("SELECT * FROM mood_entries ORDER BY dateIso DESC LIMIT :limit")
    suspend fun getRecent(limit: Int): List<MoodEntryEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(entity: MoodEntryEntity)

    @Query("""
        UPDATE mood_entries
        SET moodScore = :moodScore,
            tagsCsv = :tagsCsv,
            note = :note,
            updatedAtEpochMs = :updatedAt
        WHERE dateIso = :dateIso
    """)
    suspend fun updateByDate(
        dateIso: String,
        moodScore: Int,
        tagsCsv: String,
        note: String?,
        updatedAt: Long
    )
}
