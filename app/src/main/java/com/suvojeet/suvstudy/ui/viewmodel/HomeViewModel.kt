package com.suvojeet.suvstudy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suvojeet.suvstudy.data.repository.StudyTaskRepository
import com.suvojeet.suvstudy.domain.model.StudyTask
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val taskRepository: StudyTaskRepository
) : ViewModel() {

    // Fetch up to 5 upcoming deadlines
    val upcomingTasks: StateFlow<List<StudyTask>> = taskRepository.getUpcomingTasks(5)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

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
                    subjectId = subjectId // Using the provided subjectId (placeholder 1L from UI for now)
                )
            )
        }
    }
}
