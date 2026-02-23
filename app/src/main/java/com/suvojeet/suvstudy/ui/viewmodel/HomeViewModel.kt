package com.suvojeet.suvstudy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suvojeet.suvstudy.data.repository.StudyTaskRepository
import com.suvojeet.suvstudy.domain.model.StudyTask
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

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
}
