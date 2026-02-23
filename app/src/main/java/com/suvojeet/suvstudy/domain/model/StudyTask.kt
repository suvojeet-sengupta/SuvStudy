package com.suvojeet.suvstudy.domain.model

import java.time.LocalDateTime

data class StudyTask(
    val id: Long = 0,
    val subjectId: Long, // Foreign key to Subject
    val title: String,
    val description: String = "",
    val dueDate: LocalDateTime? = null,
    val isCompleted: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
