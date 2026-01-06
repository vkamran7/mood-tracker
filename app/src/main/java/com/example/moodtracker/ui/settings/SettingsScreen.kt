package com.example.moodtracker.ui.settings

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moodtracker.data.PreferencesManager
import com.example.moodtracker.ui.notifications.NotificationScheduler

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    preferencesManager: PreferencesManager
) {
    val vm: SettingsViewModel = viewModel(factory = SettingsViewModel.factory(preferencesManager))
    val state by vm.state.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium
        )

        // Notifications Section
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Notifications",
                    style = MaterialTheme.typography.titleLarge
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Daily reminders")
                    Switch(
                        checked = state.notificationsEnabled,
                        onCheckedChange = { enabled ->
                            vm.setNotificationsEnabled(enabled)
                            if (enabled) {
                                NotificationScheduler.scheduleDailyReminder(context, state.notificationHour, state.notificationMinute)
                            } else {
                                NotificationScheduler.cancelDailyReminder(context)
                            }
                        }
                    )
                }

                if (state.notificationsEnabled) {
                    Text("Reminder time: ${String.format("%02d:%02d", state.notificationHour, state.notificationMinute)}")
                    Button(
                        onClick = { vm.showTimePicker() }
                    ) {
                        Text("Change time")
                    }
                }
            }
        }

        // Theme Section
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Appearance",
                    style = MaterialTheme.typography.titleLarge
                )

                Text("Theme mode")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = state.themeMode == "system",
                        onClick = { vm.setThemeMode("system") },
                        label = { Text("System") }
                    )
                    FilterChip(
                        selected = state.themeMode == "light",
                        onClick = { vm.setThemeMode("light") },
                        label = { Text("Light") }
                    )
                    FilterChip(
                        selected = state.themeMode == "dark",
                        onClick = { vm.setThemeMode("dark") },
                        label = { Text("Dark") }
                    )
                }
            }
        }

        // About Section
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "About",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Mood Tracker v1.0",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Track your daily moods and emotions",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }

    // Time Picker Dialog
    if (state.showTimePicker) {
        TimePickerDialog(
            initialHour = state.notificationHour,
            initialMinute = state.notificationMinute,
            onTimeSelected = { hour, minute ->
                vm.setNotificationTime(hour, minute)
                NotificationScheduler.scheduleDailyReminder(context, hour, minute)
            },
            onDismiss = { vm.hideTimePicker() }
        )
    }
}

@Composable
private fun TimePickerDialog(
    initialHour: Int,
    initialMinute: Int,
    onTimeSelected: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    var hour by remember { mutableStateOf(initialHour) }
    var minute by remember { mutableStateOf(initialMinute) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select reminder time") },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = { if (hour < 23) hour++ }) {
                        Text("+")
                    }
                    Text("${String.format("%02d", hour)}", style = MaterialTheme.typography.headlineMedium)
                    Button(onClick = { if (hour > 0) hour-- }) {
                        Text("-")
                    }
                    Text(":", style = MaterialTheme.typography.headlineMedium)
                    Button(onClick = { if (minute < 59) minute++ }) {
                        Text("+")
                    }
                    Text("${String.format("%02d", minute)}", style = MaterialTheme.typography.headlineMedium)
                    Button(onClick = { if (minute > 0) minute-- }) {
                        Text("-")
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onTimeSelected(hour, minute) }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

