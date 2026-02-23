package com.suvojeet.suvstudy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suvojeet.suvstudy.data.repository.FocusSessionRepository
import com.suvojeet.suvstudy.data.repository.StudyTaskRepository
import com.suvojeet.suvstudy.data.repository.SubjectRepository
import com.suvojeet.suvstudy.domain.manager.TimerManager
import com.suvojeet.suvstudy.domain.model.FocusSession
import com.suvojeet.suvstudy.domain.model.StudyTask
import com.suvojeet.suvstudy.domain.model.Subject
import com.suvojeet.suvstudy.domain.manager.SettingsManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class HomeViewModel(
    private val taskRepository: StudyTaskRepository,
    private val subjectRepository: SubjectRepository,
    private val focusSessionRepository: FocusSessionRepository,
    private val timerManager: TimerManager,
    private val settingsManager: SettingsManager
) : ViewModel() {

    // Fetch up to 5 upcoming deadlines
    val upcomingTasks: StateFlow<List<StudyTask>> = taskRepository.getUpcomingTasks(5)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val subjects: StateFlow<List<Subject>> = subjectRepository.getAllSubjects()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val timerState = timerManager.timerState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = com.suvojeet.suvstudy.domain.manager.TimerState()
        )

    val pomodoroWorkMins = settingsManager.pomodoroWorkMins.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 25
    )

    fun startTimer(task: StudyTask, isPomodoro: Boolean = false) {
        val workMins = pomodoroWorkMins.value
        timerManager.startTimer(task.id, task.title, task.subjectId, isPomodoro, workMins)
    }

    fun resumeTimer() {
        val state = timerState.value
        val workMins = pomodoroWorkMins.value
        if (state.activeTaskId != null && state.taskTitle != null && state.subjectId != null) {
            timerManager.startTimer(state.activeTaskId, state.taskTitle, state.subjectId, state.isPomodoroMode, workMins)
        }
    }

    fun pauseTimer() {
        timerManager.pauseTimer()
    }

    fun stopTimer() {
        viewModelScope.launch {
            val (finalState, minutesSpent) = timerManager.stopTimerAndGetSession()
            if (finalState.activeTaskId != null && finalState.subjectId != null && minutesSpent > 0) {
                // Update timeSpent in Task
                val task = taskRepository.getTaskById(finalState.activeTaskId)
                if (task != null) {
                    taskRepository.updateTask(task.copy(timeSpentMinutes = task.timeSpentMinutes + minutesSpent))
                }
                
                // Create FocusSession for the goal tracking
                focusSessionRepository.insertSession(
                    FocusSession(
                        subjectId = finalState.subjectId,
                        taskId = finalState.activeTaskId,
                        durationMinutes = minutesSpent,
                        startTime = LocalDateTime.now().minusMinutes(minutesSpent.toLong()),
                        endTime = LocalDateTime.now(),
                        notes = "Studied ${finalState.taskTitle} for $minutesSpent mins"
                    )
                )
            }
        }
    }

    fun toggleTaskCompletion(task: StudyTask) {
        viewModelScope.launch {
            taskRepository.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }

    fun addTask(title: String, description: String, subjectId: Long) {
        viewModelScope.launch {
            taskRepository.insertTask(
                StudyTask(
                    title = title,
                    description = description,
                    subjectId = subjectId 
                )
            )
        }
    }

    fun deleteTask(task: StudyTask) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }
    }
}
