package com.example.myapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.models.Answer
import com.example.myapp.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AnswerViewModel(private val repository: FirebaseRepository) : ViewModel() {
    private val _answers = MutableStateFlow<List<Answer>>(emptyList())
    val answers: StateFlow<List<Answer>> get() = _answers

    fun fetchAnswers(questionId: String) {
        viewModelScope.launch {
            _answers.value = repository.getAnswers(questionId)
        }
    }

    fun addAnswer(answer: Answer) {
        viewModelScope.launch {
            repository.addAnswer(answer)
            fetchAnswers(answer.questionId) // Refresh the list
        }
    }
}
