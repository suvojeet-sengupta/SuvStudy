package com.suvojeet.suvstudy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suvojeet.suvstudy.data.repository.GoalRepository
import com.suvojeet.suvstudy.domain.model.Goal
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GoalsViewModel(
    private val goalRepository: GoalRepository
) : ViewModel() {

    val goals: StateFlow<List<Goal>> = goalRepository.getAllGoals()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addGoal(title: String, targetScore: Float, unit: String) {
        viewModelScope.launch {
            goalRepository.insertGoal(
                Goal(
                    title = title,
                    targetScore = targetScore,
                    unit = unit
                )
            )
        }
    }
    
    fun updateProgress(goal: Goal, newScore: Float) {
        viewModelScope.launch {
            goalRepository.updateGoal(goal.copy(currentScore = newScore))
        }
    }
}
