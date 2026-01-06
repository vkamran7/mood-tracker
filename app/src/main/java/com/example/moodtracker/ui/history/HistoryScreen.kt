package com.example.moodtracker.ui.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moodtracker.data.MoodRepository
import com.example.moodtracker.domain.Mood

@Composable
fun HistoryScreen(repo: MoodRepository) {
    val vm: HistoryViewModel = viewModel(factory = HistoryViewModel.factory(repo))
    val state by vm.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("History", style = MaterialTheme.typography.headlineMedium)

        if (state.items.isEmpty()) {
            Text("No entries yet. Tap a mood on Today.", style = MaterialTheme.typography.bodyLarge)
            return
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(state.items, key = { it.id }) { item ->
                val mood = Mood.fromScore(item.moodScore)
                Card {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(item.dateIso, style = MaterialTheme.typography.titleMedium)
                            val tags = item.tagsCsv.takeIf { it.isNotBlank() } ?: "—"
                            Text("Tags: $tags", style = MaterialTheme.typography.bodyMedium)
                            if (!item.note.isNullOrBlank()) {
                                Text(item.note!!, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                        Text(mood.emoji, style = MaterialTheme.typography.headlineLarge)
                    }
                }
            }
        }
    }
}
