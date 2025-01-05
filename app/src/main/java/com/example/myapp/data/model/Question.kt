package com.example.myapp.models

data class Question(
    var id: String = "",
    val title: String = "",
    val description: String = "",
    val authorId: String = "",
    val timestamp: Long = 0L
)
