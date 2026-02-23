package com.suvojeet.suvstudy.domain.manager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsManager(private val context: Context) {

    companion object {
        val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
        val POMODORO_WORK_KEY = intPreferencesKey("pomodoro_work_mins")
        val REMINDER_HOUR_KEY = intPreferencesKey("reminder_hour")
        val REMINDER_MINUTE_KEY = intPreferencesKey("reminder_minute")
    }

    val isDarkMode: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[DARK_MODE_KEY] ?: false // Default to false / system theme
    }

    val pomodoroWorkMins: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[POMODORO_WORK_KEY] ?: 25
    }

    val reminderHour: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[REMINDER_HOUR_KEY] ?: 18 // Default 6 PM
    }

    val reminderMinute: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[REMINDER_MINUTE_KEY] ?: 0
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = enabled
        }
    }

    suspend fun setPomodoroWorkMins(mins: Int) {
        context.dataStore.edit { preferences ->
            preferences[POMODORO_WORK_KEY] = mins
        }
    }

    suspend fun setReminderTime(hour: Int, minute: Int) {
        context.dataStore.edit { preferences ->
            preferences[REMINDER_HOUR_KEY] = hour
            preferences[REMINDER_MINUTE_KEY] = minute
        }
    }
}
