package com.suvojeet.suvstudy.data.local.dao

import androidx.room.*
import com.suvojeet.suvstudy.data.local.entity.FocusSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FocusDao {
    @Query("SELECT * FROM focus_sessions ORDER BY startTime DESC")
    fun getAllSessions(): Flow<List<FocusSessionEntity>>
    
    @Query("SELECT * FROM focus_sessions WHERE subjectId = :subjectId ORDER BY startTime DESC")
    fun getSessionsForSubject(subjectId: Long): Flow<List<FocusSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: FocusSessionEntity): Long

    @Delete
    suspend fun deleteSession(session: FocusSessionEntity)
}
