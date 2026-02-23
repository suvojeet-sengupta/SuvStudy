package com.suvojeet.suvstudy.domain.model

data class Goal(
    val id: Long = 0,
    val title: String,
    val targetScore: Float,
    val currentScore: Float = 0f,
    val unit: String = "%" // e.g. %, GPA, etc.
) {
    val progress: Float
        get() = if (targetScore > 0) currentScore / targetScore else 0f
}
