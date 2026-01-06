package com.example.moodtracker

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.moodtracker.data.DatabaseProvider
import com.example.moodtracker.data.MoodRepository
import com.example.moodtracker.data.PreferencesManager
import com.example.moodtracker.ui.App
import com.example.moodtracker.ui.notifications.NotificationScheduler
import com.example.moodtracker.ui.theme.MoodTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = PreferencesManager(this)
        
        // Initialize notifications if enabled
        if (prefs.notificationsEnabled) {
            NotificationScheduler.scheduleDailyReminder(
                this,
                prefs.notificationHour,
                prefs.notificationMinute
            )
        }

        setContent {
            val context = LocalContext.current
            val db = remember { DatabaseProvider.get(context) }
            val repo = remember { MoodRepository(db.moodDao()) }
            val preferencesManager = remember { PreferencesManager(context) }
            
            // Reactive theme mode state
            var themeMode by remember { mutableStateOf(preferencesManager.themeMode) }
            
            // Listen for preference changes
            DisposableEffect(Unit) {
                val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                    if (key == "theme_mode") {
                        themeMode = preferencesManager.themeMode
                    }
                }
                preferencesManager.preferences.registerOnSharedPreferenceChangeListener(listener)
                onDispose {
                    preferencesManager.preferences.unregisterOnSharedPreferenceChangeListener(listener)
                }
            }
            
            val darkTheme = when (themeMode) {
                "dark" -> true
                "light" -> false
                else -> isSystemInDarkTheme()
            }

            MoodTrackerTheme(darkTheme = darkTheme) {
                Surface {
                    App(repo = repo)
                }
            }
        }
    }
}
