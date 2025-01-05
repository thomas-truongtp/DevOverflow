package com.example.myapp.models

data class Answer(
    val id: String = "",
    val questionId: String = "",
    val text: String = "",
    val authorId: String = "",
    val timestamp: Long = 0L
)
