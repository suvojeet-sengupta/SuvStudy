package com.suvojeet.suvstudy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suvojeet.suvstudy.data.repository.SubjectRepository
import com.suvojeet.suvstudy.domain.model.Subject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SubjectsViewModel(
    private val subjectRepository: SubjectRepository
) : ViewModel() {

    val subjects: StateFlow<List<Subject>> = subjectRepository.getAllSubjects()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addSubject(name: String, description: String, totalChapters: Int) {
        viewModelScope.launch {
            subjectRepository.insertSubject(
                Subject(
                    name = name,
                    description = description,
                    totalChapters = totalChapters
                )
            )
        }
    }
}
