package com.suvojeet.suvstudy.data.mapper

import com.suvojeet.suvstudy.data.local.entity.FocusSessionEntity
import com.suvojeet.suvstudy.data.local.entity.GoalEntity
import com.suvojeet.suvstudy.data.local.entity.StudyTaskEntity
import com.suvojeet.suvstudy.data.local.entity.SubjectEntity
import com.suvojeet.suvstudy.domain.model.FocusSession
import com.suvojeet.suvstudy.domain.model.Goal
import com.suvojeet.suvstudy.domain.model.StudyTask
import com.suvojeet.suvstudy.domain.model.Subject
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

// --- Subject Mappers ---
fun SubjectEntity.toDomain(): Subject {
    return Subject(
        id = id,
        name = name,
        description = description,
        totalChapters = totalChapters,
        completedChapters = completedChapters,
        colorHex = colorHex,
        dailyGoalMinutes = dailyGoalMinutes
    )
}

fun Subject.toEntity(): SubjectEntity {
    return SubjectEntity(
        id = id,
        name = name,
        description = description,
        totalChapters = totalChapters,
        completedChapters = completedChapters,
        colorHex = colorHex,
        dailyGoalMinutes = dailyGoalMinutes
    )
}

// --- StudyTask Mappers ---
fun StudyTaskEntity.toDomain(): StudyTask {
    return StudyTask(
        id = id,
        subjectId = subjectId,
        title = title,
        description = description,
        dueDate = dueDate?.let { LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault()) },
        isCompleted = isCompleted,
        createdAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(createdAt), ZoneId.systemDefault()),
        timeSpentMinutes = timeSpentMinutes
    )
}

fun StudyTask.toEntity(): StudyTaskEntity {
    return StudyTaskEntity(
        id = id,
        subjectId = subjectId,
        title = title,
        description = description,
        dueDate = dueDate?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli(),
        isCompleted = isCompleted,
        createdAt = createdAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        timeSpentMinutes = timeSpentMinutes
    )
}

// --- FocusSession Mappers ---
fun FocusSessionEntity.toDomain(): FocusSession {
    return FocusSession(
        id = id,
        subjectId = subjectId,
        taskId = taskId,
        durationMinutes = durationMinutes,
        startTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime), ZoneId.systemDefault()),
        endTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(endTime), ZoneId.systemDefault()),
        notes = notes
    )
}

fun FocusSession.toEntity(): FocusSessionEntity {
    return FocusSessionEntity(
        id = id,
        subjectId = subjectId,
        taskId = taskId,
        durationMinutes = durationMinutes,
        startTime = startTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        endTime = endTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        notes = notes
    )
}

// --- Goal Mappers ---
fun GoalEntity.toDomain(): Goal {
    return Goal(
        id = id,
        title = title,
        targetScore = targetScore,
        currentScore = currentScore,
        unit = unit
    )
}

fun Goal.toEntity(): GoalEntity {
    return GoalEntity(
        id = id,
        title = title,
        targetScore = targetScore,
        currentScore = currentScore,
        unit = unit
    )
}
