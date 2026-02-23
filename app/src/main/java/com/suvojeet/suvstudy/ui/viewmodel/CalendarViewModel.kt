package com.suvojeet.suvstudy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suvojeet.suvstudy.data.repository.StudyTaskRepository
import com.suvojeet.suvstudy.domain.model.StudyTask
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class CalendarViewModel(
    private val taskRepository: StudyTaskRepository
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    // All active or completed tasks (up to a limit, or all for calendar context - let's fetch all for simplicity)
    val tasksByDate = taskRepository.getUpcomingTasks(100).map { tasks ->
        tasks.filter { it.dueDate != null }
             .groupBy { it.dueDate!!.toLocalDate() }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyMap()
    )

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }
    
    fun toggleTaskCompletion(task: StudyTask) {
        viewModelScope.launch {
            taskRepository.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }
}
