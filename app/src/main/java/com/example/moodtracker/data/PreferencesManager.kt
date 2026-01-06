package com.example.moodtracker.data

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    val preferences: SharedPreferences = context.getSharedPreferences(
        "mood_tracker_prefs",
        Context.MODE_PRIVATE
    )
    
    private val prefs: SharedPreferences = preferences

    companion object {
        private const val KEY_NOTIFICATIONS_ENABLED = "notifications_enabled"
        private const val KEY_NOTIFICATION_HOUR = "notification_hour"
        private const val KEY_NOTIFICATION_MINUTE = "notification_minute"
        private const val KEY_DARK_THEME = "dark_theme"
        private const val KEY_THEME_MODE = "theme_mode" // "system", "light", "dark"
    }

    var notificationsEnabled: Boolean
        get() = prefs.getBoolean(KEY_NOTIFICATIONS_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_NOTIFICATIONS_ENABLED, value).apply()

    var notificationHour: Int
        get() = prefs.getInt(KEY_NOTIFICATION_HOUR, 20) // 8 PM default
        set(value) = prefs.edit().putInt(KEY_NOTIFICATION_HOUR, value).apply()

    var notificationMinute: Int
        get() = prefs.getInt(KEY_NOTIFICATION_MINUTE, 0)
        set(value) = prefs.edit().putInt(KEY_NOTIFICATION_MINUTE, value).apply()

    var themeMode: String
        get() = prefs.getString(KEY_THEME_MODE, "system") ?: "system"
        set(value) = prefs.edit().putString(KEY_THEME_MODE, value).apply()
}

