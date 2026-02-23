package com.suvojeet.suvstudy.domain.manager

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TimerState(
    val activeTaskId: Long? = null,
    val taskTitle: String? = null,
    val subjectId: Long? = null,
    val elapsedSeconds: Long = 0,
    val isRunning: Boolean = false
)

class TimerManager {
    private val _timerState = MutableStateFlow(TimerState())
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    private var timerJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    fun startTimer(taskId: Long, taskTitle: String, subjectId: Long) {
        if (_timerState.value.activeTaskId == taskId && _timerState.value.isRunning) return
        
        // If starting a new different task, reset time. 
        // Note: For a real app, we'd fetch the existing timeSpent from DB, but for now we track current session.
        if (_timerState.value.activeTaskId != taskId) {
            _timerState.update { 
                it.copy(
                    activeTaskId = taskId, 
                    taskTitle = taskTitle, 
                    subjectId = subjectId,
                    elapsedSeconds = 0, 
                    isRunning = true 
                ) 
            }
        } else {
            // Resume
            _timerState.update { it.copy(isRunning = true) }
        }

        timerJob?.cancel()
        timerJob = scope.launch {
            while (_timerState.value.isRunning) {
                delay(1000)
                _timerState.update { it.copy(elapsedSeconds = it.elapsedSeconds + 1) }
            }
        }
    }

    fun pauseTimer() {
        _timerState.update { it.copy(isRunning = false) }
        timerJob?.cancel()
    }

    fun stopTimerAndGetSession(): Pair<TimerState, Int> {
        val finalState = _timerState.value
        val minutesSpent = (finalState.elapsedSeconds / 60).toInt()
        
        pauseTimer()
        _timerState.update { TimerState() } // Reset
        
        return Pair(finalState, minutesSpent)
    }
}
