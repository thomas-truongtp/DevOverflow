package com.example.myapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.models.Question
import com.example.myapp.repository.FirebaseRepository
import kotlinx.coroutines.launch

class QuestionViewModel(private val repository: FirebaseRepository = FirebaseRepository()) : ViewModel() {

    fun addQuestion(question: Question) {
        viewModelScope.launch {
            repository.addQuestion(question)
        }
    }
}
