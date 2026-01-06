package com.example.moodtracker.ui.statistics

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

@Composable
fun StatisticsScreen(repo: MoodRepository) {
    val vm: StatisticsViewModel = viewModel(factory = StatisticsViewModel.factory(repo))
    val state by vm.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Statistics",
            style = MaterialTheme.typography.headlineMedium
        )

        if (state.totalEntries == 0) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "No data yet. Start tracking your moods!",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            return
        }

        // Total entries
        StatCard(
            title = "Total Entries",
            value = state.totalEntries.toString(),
            modifier = Modifier.fillMaxWidth()
        )

        // Mood distribution
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Mood Distribution",
                    style = MaterialTheme.typography.titleLarge
                )
                Mood.entries.forEach { mood ->
                    val count = state.moodCounts[mood] ?: 0
                    val percentage = if (state.totalEntries > 0) {
                        (count * 100 / state.totalEntries)
                    } else 0
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("${mood.emoji} ${mood.title}")
                        Text("$count ($percentage%)")
                    }
                    LinearProgressIndicator(
                        progress = { count.toFloat() / state.totalEntries.coerceAtLeast(1) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Average mood
        StatCard(
            title = "Average Mood",
            value = state.averageMood?.emoji ?: "—",
            subtitle = state.averageMood?.title ?: "",
            modifier = Modifier.fillMaxWidth()
        )

        // Streak
        StatCard(
            title = "Current Streak",
            value = state.currentStreak.toString(),
            subtitle = "days",
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    subtitle: String = "",
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
            if (subtitle.isNotEmpty()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

