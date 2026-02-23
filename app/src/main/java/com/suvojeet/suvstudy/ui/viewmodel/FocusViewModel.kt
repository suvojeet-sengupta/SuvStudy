package com.suvojeet.suvstudy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suvojeet.suvstudy.data.repository.FocusSessionRepository
import com.suvojeet.suvstudy.domain.model.FocusSession
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class FocusViewModel(
    private val focusSessionRepository: FocusSessionRepository
) : ViewModel() {

    val recentSessions: StateFlow<List<FocusSession>> = focusSessionRepository.getAllSessions()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

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
