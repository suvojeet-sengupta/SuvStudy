package com.suvojeet.suvstudy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suvojeet.suvstudy.data.repository.FocusSessionRepository
import com.suvojeet.suvstudy.data.repository.SubjectRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class SubjectStudyStat(
    val subjectName: String,
    val totalMinutes: Int,
    val colorIndex: Int
)

class InsightsViewModel(
    focusSessionRepository: FocusSessionRepository,
    subjectRepository: SubjectRepository
) : ViewModel() {

    // A simple aggregated stat for this week
    val weeklyStats = combine(
        focusSessionRepository.getAllSessions(),
        subjectRepository.getAllSubjects()
    ) { sessions, subjects ->
        val now = LocalDateTime.now()
        val startOfWeek = now.minusDays(7)
        
        // Filter sessions to last 7 days
        val recentSessions = sessions.filter { it.startTime.isAfter(startOfWeek) }
        
        // Group by subjectId and sum duration
        val grouped = recentSessions.groupBy { it.subjectId }
        
        subjects.mapIndexed { index, subject ->
            val totalMins = grouped[subject.id]?.sumOf { it.durationMinutes } ?: 0
            SubjectStudyStat(
                subjectName = subject.name,
                totalMinutes = totalMins,
                colorIndex = index
            )
        }.sortedByDescending { it.totalMinutes }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
}
