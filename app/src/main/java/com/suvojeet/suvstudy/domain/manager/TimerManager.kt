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
    val elapsedSeconds: Long = 0, // In pomodoro mode this is countdown; in stopwatch it is count up
    val originalDurationSeconds: Long = 0, 
    val isRunning: Boolean = false,
    val isPomodoroMode: Boolean = false,
    val isBreak: Boolean = false
)

class TimerManager {
    private val _timerState = MutableStateFlow(TimerState())
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    private var timerJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    fun startTimer(taskId: Long, taskTitle: String, subjectId: Long, isPomodoro: Boolean = false, pomodoroWorkMins: Int = 25) {
        if (_timerState.value.activeTaskId == taskId && _timerState.value.isRunning) return
        
        if (_timerState.value.activeTaskId != taskId || _timerState.value.isPomodoroMode != isPomodoro) {
            val startSeconds = if (isPomodoro) (pomodoroWorkMins * 60).toLong() else 0L
            _timerState.update { 
                it.copy(
                    activeTaskId = taskId, 
                    taskTitle = taskTitle, 
                    subjectId = subjectId,
                    elapsedSeconds = startSeconds, 
                    originalDurationSeconds = startSeconds,
                    isRunning = true,
                    isPomodoroMode = isPomodoro,
                    isBreak = false
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
                _timerState.update { state -> 
                    if (state.isPomodoroMode) {
                        val newSeconds = state.elapsedSeconds - 1
                        if (newSeconds <= 0) {
                            // Cycle ended
                            if (!state.isBreak) {
                                // Start 5 min break
                                state.copy(isBreak = true, elapsedSeconds = 5 * 60)
                            } else {
                                // Break ended, stop timer
                                state.copy(isRunning = false, elapsedSeconds = 0)
                            }
                        } else {
                            state.copy(elapsedSeconds = newSeconds)
                        }
                    } else {
                        state.copy(elapsedSeconds = state.elapsedSeconds + 1)
                    }
                }
            }
        }
    }

    fun pauseTimer() {
        _timerState.update { it.copy(isRunning = false) }
        timerJob?.cancel()
    }

    fun stopTimerAndGetSession(): Pair<TimerState, Int> {
        val finalState = _timerState.value
        val minutesSpent = if (finalState.isPomodoroMode) {
            val elapsedWork = finalState.originalDurationSeconds - finalState.elapsedSeconds
            (Math.max(0, elapsedWork) / 60).toInt()
        } else {
            (finalState.elapsedSeconds / 60).toInt()
        }
        
        pauseTimer()
        _timerState.update { TimerState() } // Reset
        
        return Pair(finalState, minutesSpent)
    }
}
