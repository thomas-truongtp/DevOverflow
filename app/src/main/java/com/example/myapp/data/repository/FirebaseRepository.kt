package com.example.myapp.repository

import com.example.myapp.models.Answer
import com.example.myapp.models.Question
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseRepository {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getQuestions(): List<Question> {
        return try {
            firestore.collection("questions")
                .get()
                .await()
                .toObjects(Question::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getAnswers(questionId: String): List<Answer> {
        return try {
            firestore.collection("answers")
                .whereEqualTo("questionId", questionId)
                .get()
                .await()
                .toObjects(Answer::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addQuestion(question: Question) {
        try {
            firestore.collection("questions")
                .add(question)
                .await()
        } catch (e: Exception) {
            // Handle any errors during Firebase interaction
        }
    }

    suspend fun addAnswer(answer: Answer) {
        firestore.collection("answers").add(answer).await()
    }
}
