package com.suvojeet.suvstudy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suvojeet.suvstudy.data.repository.FocusSessionRepository
import com.suvojeet.suvstudy.domain.model.FocusSession
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

class FocusViewModel(
    private val focusSessionRepository: FocusSessionRepository
) : ViewModel() {

    val recentSessions: StateFlow<List<FocusSession>> = focusSessionRepository.getAllSessions()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _timerState = MutableStateFlow(TimerState.IDLE)
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    private val _timeLeftInSeconds = MutableStateFlow(25 * 60)
    val timeLeftInSeconds: StateFlow<Int> = _timeLeftInSeconds.asStateFlow()

    private var timerJob: Job? = null
    private var sessionStartTime: LocalDateTime? = null
    private var durationMinutes: Int = 25
    private var currentSubjectId: Long? = null

    fun startTimer(subjectId: Long? = null) {
        if (_timerState.value == TimerState.RUNNING) return
        
        if (_timerState.value == TimerState.IDLE) {
            sessionStartTime = LocalDateTime.now()
            currentSubjectId = subjectId
        }
        
        _timerState.value = TimerState.RUNNING
        
        timerJob = viewModelScope.launch {
            while (_timeLeftInSeconds.value > 0) {
                delay(1000)
                _timeLeftInSeconds.value -= 1
            }
            // Timer Finished
            finishSession()
        }
    }

    fun pauseTimer() {
        if (_timerState.value != TimerState.RUNNING) return
        timerJob?.cancel()
        _timerState.value = TimerState.PAUSED
    }

    fun stopTimer() {
        timerJob?.cancel()
        _timerState.value = TimerState.IDLE
        _timeLeftInSeconds.value = durationMinutes * 60
    }

    private fun finishSession() {
        stopTimer()
        
        // Log the completed session to the database
        sessionStartTime?.let { start ->
            saveSession(
                subjectId = currentSubjectId ?: 1L, // Placeholder: Needs real subject picker eventually
                durationMinutes = durationMinutes,
                startTime = start,
                endTime = LocalDateTime.now()
            )
        }
    }

    fun saveSession(subjectId: Long, durationMinutes: Int, startTime: LocalDateTime, endTime: LocalDateTime) {
        viewModelScope.launch {
            focusSessionRepository.insertSession(
                FocusSession(
                    subjectId = subjectId,
                    durationMinutes = durationMinutes,
                    startTime = startTime,
                    endTime = endTime
                )
            )
        }
    }
}

enum class TimerState {
    IDLE, RUNNING, PAUSED
}
