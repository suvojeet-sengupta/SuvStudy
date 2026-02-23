package com.suvojeet.suvstudy.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
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
data class StudyTaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val subjectId: Long,
    val title: String,
    val description: String,
    val dueDate: Long?, // Stored as timestamp
    val isCompleted: Boolean,
    val createdAt: Long
)
