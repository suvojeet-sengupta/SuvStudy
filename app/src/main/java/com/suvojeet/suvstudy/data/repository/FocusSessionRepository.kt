package com.suvojeet.suvstudy.data.repository

import com.suvojeet.suvstudy.data.local.dao.FocusDao
import com.suvojeet.suvstudy.data.mapper.toDomain
import com.suvojeet.suvstudy.data.mapper.toEntity
import com.suvojeet.suvstudy.domain.model.FocusSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FocusSessionRepository(private val dao: FocusDao) {
    fun getAllSessions(): Flow<List<FocusSession>> {
        return dao.getAllSessions().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    fun getSessionsForSubject(subjectId: Long): Flow<List<FocusSession>> {
        return dao.getSessionsForSubject(subjectId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun insertSession(session: FocusSession): Long {
        return dao.insertSession(session.toEntity())
    }

    suspend fun deleteSession(session: FocusSession) {
        dao.deleteSession(session.toEntity())
    }
}
