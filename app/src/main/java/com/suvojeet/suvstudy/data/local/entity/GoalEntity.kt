package com.suvojeet.suvstudy.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val targetScore: Float,
    val currentScore: Float,
    val unit: String
)
