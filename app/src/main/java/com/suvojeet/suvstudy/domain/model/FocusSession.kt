package com.suvojeet.suvstudy.domain.model

import java.time.LocalDateTime

data class FocusSession(
    val id: Long = 0,
    val subjectId: Long, // Associated subject
    val taskId: Long? = null, // Optional associated task
    val durationMinutes: Int, 
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val notes: String = ""
)
