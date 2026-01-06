package com.example.moodtracker.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.moodtracker.data.MoodEntryEntity
import com.example.moodtracker.data.MoodRepository
import com.example.moodtracker.domain.Mood
import com.example.moodtracker.domain.Tags
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = true,
    val todayEntry: MoodEntryEntity? = null,
    val selectedMood: Mood? = null,
    val selectedTags: Set<String> = emptySet(),
    val note: String = "",
    val showDetailsSheet: Boolean = false
)

@RequiresApi(Build.VERSION_CODES.O)
class HomeViewModel(private val repo: MoodRepository) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state

    init { load() }

    @RequiresApi(Build.VERSION_CODES.O)
    fun load() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }
        val today = repo.getToday()
        _state.update {
            val mood = today?.let { e -> Mood.fromScore(e.moodScore) }
            val tags = today?.tagsCsv?.takeIf { it.isNotBlank() }?.split(",")?.map { it.trim() }?.toSet() ?: emptySet()
            it.copy(
                isLoading = false,
                todayEntry = today,
                selectedMood = mood,
                selectedTags = tags,
                note = today?.note.orEmpty(),
                showDetailsSheet = false
            )
        }
    }

    fun onMoodTap(mood: Mood) = viewModelScope.launch {
        _state.update { it.copy(selectedMood = mood) }
        // Auto-save immediately with current tags/note
        val current = _state.value
        repo.upsertToday(mood, current.selectedTags.toList(), current.note)
        // Refresh to sync entity
        load()
    }

    fun openDetails() {
        _state.update { it.copy(showDetailsSheet = true) }
    }

    fun closeDetails() {
        _state.update { it.copy(showDetailsSheet = false) }
    }

    fun toggleTag(tag: String) {
        _state.update {
            val newSet = it.selectedTags.toMutableSet()
            if (!newSet.add(tag)) newSet.remove(tag)
            it.copy(selectedTags = newSet)
        }
    }

    fun updateNote(note: String) {
        _state.update { it.copy(note = note.take(120)) }
    }

    fun saveDetails() = viewModelScope.launch {
        val s = _state.value
        val mood = s.selectedMood ?: return@launch
        repo.upsertToday(mood, s.selectedTags.toList(), s.note)
        load()
    }

    companion object {
        fun factory(repo: MoodRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HomeViewModel(repo) as T
                }
            }
    }
}
