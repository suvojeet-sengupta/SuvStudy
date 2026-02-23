package com.suvojeet.suvstudy.data.repository

import com.suvojeet.suvstudy.data.local.dao.SubjectDao
import com.suvojeet.suvstudy.data.mapper.toDomain
import com.suvojeet.suvstudy.data.mapper.toEntity
import com.suvojeet.suvstudy.domain.model.Subject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SubjectRepository(private val dao: SubjectDao) {
    fun getAllSubjects(): Flow<List<Subject>> {
        return dao.getAllSubjects().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    fun getSubjectById(id: Long): Flow<Subject?> {
        return dao.getSubjectById(id).map { it?.toDomain() }
    }

    suspend fun insertSubject(subject: Subject): Long {
        return dao.insertSubject(subject.toEntity())
    }

    suspend fun updateSubject(subject: Subject) {
        dao.updateSubject(subject.toEntity())
    }

    suspend fun deleteSubject(subject: Subject) {
        dao.deleteSubject(subject.toEntity())
    }
}
