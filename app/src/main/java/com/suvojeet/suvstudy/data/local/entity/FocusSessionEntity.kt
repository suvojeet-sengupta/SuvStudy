package com.suvojeet.suvstudy.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "focus_sessions",
    foreignKeys = [
        ForeignKey(
            entity = SubjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("subjectId")]
)
data class FocusSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val subjectId: Long,
    val taskId: Long? = null,
    val durationMinutes: Int,
    val startTime: Long, // Stored as timestamp
    val endTime: Long,
    val notes: String
)
