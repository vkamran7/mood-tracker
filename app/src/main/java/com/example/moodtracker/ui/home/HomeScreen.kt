package com.example.moodtracker.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moodtracker.data.MoodRepository
import com.example.moodtracker.domain.Mood
import com.example.moodtracker.domain.Tags
import com.example.moodtracker.util.todayIso

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(repo: MoodRepository) {
    val vm: HomeViewModel = viewModel(factory = HomeViewModel.factory(repo))
    val state by vm.state.collectAsState()

    if (state.showDetailsSheet) {
        DetailsSheet(
            selectedTags = state.selectedTags,
            note = state.note,
            onToggleTag = vm::toggleTag,
            onNoteChange = vm::updateNote,
            onSave = { vm.saveDetails() },
            onDismiss = vm::closeDetails
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "How was today?",
            style = MaterialTheme.typography.headlineMedium
        )

        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Tap one mood. You're done.",
                    style = MaterialTheme.typography.bodyLarge
                )

                MoodRow(
                    selected = state.selectedMood,
                    onTap = vm::onMoodTap
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val current = state.selectedMood?.let { "${it.emoji} ${it.title}" } ?: "No mood yet"
                    Text(text = current, style = MaterialTheme.typography.bodyMedium)

                    TextButton(
                        enabled = state.selectedMood != null,
                        onClick = { vm.openDetails() }
                    ) {
                        Text("Edit details")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("Today: ${todayIso()}", style = MaterialTheme.typography.bodyMedium)
                if (state.todayEntry != null) {
                    Text(
                        text = "Saved ✅",
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    Text(
                        text = "Not saved yet",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun MoodRow(
    selected: Mood?,
    onTap: (Mood) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Mood.entries.forEach { mood ->
            val isSelected = selected == mood
            FilledTonalButton(
                onClick = { onTap(mood) },
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = mood.emoji,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}
