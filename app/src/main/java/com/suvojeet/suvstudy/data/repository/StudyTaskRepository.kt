package com.suvojeet.suvstudy.data.repository

import com.suvojeet.suvstudy.data.local.dao.StudyTaskDao
import com.suvojeet.suvstudy.data.mapper.toDomain
import com.suvojeet.suvstudy.data.mapper.toEntity
import com.suvojeet.suvstudy.domain.model.StudyTask
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StudyTaskRepository(private val dao: StudyTaskDao) {
    fun getAllTasks(): Flow<List<StudyTask>> {
        return dao.getAllTasks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    fun getTasksForSubject(subjectId: Long): Flow<List<StudyTask>> {
        return dao.getTasksForSubject(subjectId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    fun getUpcomingTasks(limit: Int = 5): Flow<List<StudyTask>> {
        return dao.getUpcomingTasks(limit).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun insertTask(task: StudyTask): Long {
        return dao.insertTask(task.toEntity())
    }

    suspend fun updateTask(task: StudyTask) {
        dao.updateTask(task.toEntity())
    }

    suspend fun deleteTask(task: StudyTask) {
        dao.deleteTask(task.toEntity())
    }
}
