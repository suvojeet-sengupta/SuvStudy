package com.suvojeet.suvstudy.data.repository

import com.suvojeet.suvstudy.data.local.dao.GoalDao
import com.suvojeet.suvstudy.data.mapper.toDomain
import com.suvojeet.suvstudy.data.mapper.toEntity
import com.suvojeet.suvstudy.domain.model.Goal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GoalRepository(private val dao: GoalDao) {
    fun getAllGoals(): Flow<List<Goal>> {
        return dao.getAllGoals().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun insertGoal(goal: Goal): Long {
        return dao.insertGoal(goal.toEntity())
    }

    suspend fun updateGoal(goal: Goal) {
        dao.updateGoal(goal.toEntity())
    }

    suspend fun deleteGoal(goal: Goal) {
        dao.deleteGoal(goal.toEntity())
    }
}
