package com.example.moodtracker.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.moodtracker.domain.Mood
import com.example.moodtracker.util.todayIso
import kotlinx.coroutines.flow.Flow
import java.util.Locale

class MoodRepository(private val dao: MoodDao) {

    fun observeAll(): Flow<List<MoodEntryEntity>> = dao.observeAll()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getToday(): MoodEntryEntity? = dao.getByDate(todayIso())

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun upsertToday(mood: Mood, tags: List<String>, note: String?) {
        val now = System.currentTimeMillis()
        val dateIso = todayIso()
        val tagsCsv = tags.joinToString(",") { it.trim() }.trim()

        val existing = dao.getByDate(dateIso)
        if (existing == null) {
            dao.insert(
                MoodEntryEntity(
                    dateIso = dateIso,
                    moodScore = mood.score,
                    tagsCsv = tagsCsv,
                    note = note?.takeIf { it.isNotBlank() },
                    createdAtEpochMs = now,
                    updatedAtEpochMs = now
                )
            )
        } else {
            dao.updateByDate(
                dateIso = dateIso,
                moodScore = mood.score,
                tagsCsv = tagsCsv,
                note = note?.takeIf { it.isNotBlank() },
                updatedAt = now
            )
        }
    }

    suspend fun updateByDate(dateIso: String, mood: Mood, tags: List<String>, note: String?) {
        val now = System.currentTimeMillis()
        val tagsCsv = tags.joinToString(",") { it.trim() }.trim()
        dao.updateByDate(
            dateIso = dateIso,
            moodScore = mood.score,
            tagsCsv = tagsCsv,
            note = note?.takeIf { it.isNotBlank() },
            updatedAt = now
        )
    }
}