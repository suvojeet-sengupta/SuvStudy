package com.suvojeet.suvstudy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suvojeet.suvstudy.domain.manager.SettingsManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsManager: SettingsManager
) : ViewModel() {

    val isDarkMode = settingsManager.isDarkMode.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        false
    )

    val pomodoroWorkMins = settingsManager.pomodoroWorkMins.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        25
    )

    val reminderHour = settingsManager.reminderHour.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        18
    )

    val reminderMinute = settingsManager.reminderMinute.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0
    )

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            settingsManager.setDarkMode(enabled)
        }
    }

    fun updatePomodoroTime(mins: Int) {
        viewModelScope.launch {
            settingsManager.setPomodoroWorkMins(mins)
        }
    }

    fun updateReminderTime(hour: Int, minute: Int) {
        viewModelScope.launch {
            settingsManager.setReminderTime(hour, minute)
        }
    }
}
