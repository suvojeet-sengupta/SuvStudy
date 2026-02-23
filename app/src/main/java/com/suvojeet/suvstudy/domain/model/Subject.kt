package com.suvojeet.suvstudy.domain.model

data class Subject(
    val id: Long = 0,
    val name: String,
    val description: String = "",
    val totalChapters: Int,
    val completedChapters: Int = 0,
    val colorHex: String? = null // For customizing card colors later
) {
    val progress: Float
        get() = if (totalChapters > 0) completedChapters.toFloat() / totalChapters.toFloat() else 0f
}
