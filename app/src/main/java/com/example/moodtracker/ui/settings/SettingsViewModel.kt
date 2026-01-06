package com.example.moodtracker.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.moodtracker.data.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SettingsUiState(
    val notificationsEnabled: Boolean = true,
    val notificationHour: Int = 20,
    val notificationMinute: Int = 0,
    val themeMode: String = "system",
    val showTimePicker: Boolean = false
)

class SettingsViewModel(private val prefs: PreferencesManager) : ViewModel() {
    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state

    init {
        load()
    }

    private fun load() = viewModelScope.launch {
        _state.update {
            it.copy(
                notificationsEnabled = prefs.notificationsEnabled,
                notificationHour = prefs.notificationHour,
                notificationMinute = prefs.notificationMinute,
                themeMode = prefs.themeMode
            )
        }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        prefs.notificationsEnabled = enabled
        _state.update { it.copy(notificationsEnabled = enabled) }
    }

    fun setNotificationTime(hour: Int, minute: Int) {
        prefs.notificationHour = hour
        prefs.notificationMinute = minute
        _state.update {
            it.copy(
                notificationHour = hour,
                notificationMinute = minute,
                showTimePicker = false
            )
        }
    }

    fun setThemeMode(mode: String) {
        prefs.themeMode = mode
        _state.update { it.copy(themeMode = mode) }
    }

    fun showTimePicker() {
        _state.update { it.copy(showTimePicker = true) }
    }

    fun hideTimePicker() {
        _state.update { it.copy(showTimePicker = false) }
    }

    companion object {
        fun factory(prefs: PreferencesManager): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SettingsViewModel(prefs) as T
                }
            }
    }
}

