package com.suvojeet.suvstudy.data.local.dao

import androidx.room.*
import com.suvojeet.suvstudy.data.local.entity.StudyTaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyTaskDao {
    @Query("SELECT * FROM tasks ORDER BY dueDate ASC")
    fun getAllTasks(): Flow<List<StudyTaskEntity>>

    @Query("SELECT * FROM tasks WHERE subjectId = :subjectId ORDER BY dueDate ASC")
    fun getTasksForSubject(subjectId: Long): Flow<List<StudyTaskEntity>>

    @Query("SELECT * FROM tasks WHERE isCompleted = 0 ORDER BY dueDate ASC LIMIT :limit")
    fun getUpcomingTasks(limit: Int): Flow<List<StudyTaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Long): StudyTaskEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: StudyTaskEntity): Long

    @Update
    suspend fun updateTask(task: StudyTaskEntity)

    @Delete
    suspend fun deleteTask(task: StudyTaskEntity)
}
