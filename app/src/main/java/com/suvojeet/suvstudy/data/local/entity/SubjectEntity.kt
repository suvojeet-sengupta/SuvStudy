package com.suvojeet.suvstudy.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subjects")
data class SubjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String,
    val totalChapters: Int,
    val completedChapters: Int,
    val colorHex: String?
)
