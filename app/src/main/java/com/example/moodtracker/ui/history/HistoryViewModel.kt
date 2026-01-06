package com.example.moodtracker.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.moodtracker.data.MoodEntryEntity
import com.example.moodtracker.data.MoodRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class HistoryUiState(
    val items: List<MoodEntryEntity> = emptyList()
)

class HistoryViewModel(repo: MoodRepository) : ViewModel() {

    val state: StateFlow<HistoryUiState> =
        repo.observeAll()
            .map { HistoryUiState(items = it) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HistoryUiState())

    companion object {
        fun factory(repo: MoodRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HistoryViewModel(repo) as T
                }
            }
    }
}
