package com.example.moodtracker.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.moodtracker.data.MoodRepository
import com.example.moodtracker.domain.Mood
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class StatisticsUiState(
    val totalEntries: Int = 0,
    val moodCounts: Map<Mood, Int> = emptyMap(),
    val averageMood: Mood? = null,
    val currentStreak: Int = 0
)

class StatisticsViewModel(private val repo: MoodRepository) : ViewModel() {
    private val _state = MutableStateFlow(StatisticsUiState())
    val state: StateFlow<StatisticsUiState> = _state

    init {
        load()
    }

    private fun load() = viewModelScope.launch {
        val allEntries = repo.observeAll()
        allEntries.collect { entries ->
            val total = entries.size
            val moodCounts = entries.groupingBy { Mood.fromScore(it.moodScore) }
                .eachCount()
            
            val averageScore = if (entries.isNotEmpty()) {
                entries.map { it.moodScore }.average().toInt()
            } else null
            val averageMood = averageScore?.let { Mood.fromScore(it) }
            
            val streak = calculateStreak(entries)
            
            _state.update {
                it.copy(
                    totalEntries = total,
                    moodCounts = moodCounts,
                    averageMood = averageMood,
                    currentStreak = streak
                )
            }
        }
    }

    private fun calculateStreak(entries: List<com.example.moodtracker.data.MoodEntryEntity>): Int {
        if (entries.isEmpty()) return 0
        
        val sortedEntries = entries.sortedByDescending { it.dateIso }
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        var streak = 0
        var currentDate = LocalDate.now()
        
        for (entry in sortedEntries) {
            val entryDate = try {
                LocalDate.parse(entry.dateIso, formatter)
            } catch (e: Exception) {
                continue
            }
            
            if (entryDate == currentDate || entryDate == currentDate.minusDays(1)) {
                if (entryDate == currentDate.minusDays(1)) {
                    currentDate = entryDate
                }
                streak++
            } else {
                break
            }
        }
        
        return streak
    }

    companion object {
        fun factory(repo: MoodRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return StatisticsViewModel(repo) as T
                }
            }
    }
}

