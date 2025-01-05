package com.example.myapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import com.example.myapp.models.Question
import com.example.myapp.viewmodels.QuestionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyQuestionsScreen(navController: NavHostController, userId: String) {
    val db = FirebaseFirestore.getInstance()
    val questions = remember { mutableStateListOf<Question>() }

    // Fetch questions for the user
    LaunchedEffect(userId) {
        db.collection("questions")
            .whereEqualTo("authorId", userId) // Fetch only the questions authored by the user
            .get()
            .addOnSuccessListener { result ->
                questions.clear()
                for (document in result) {
                    val question = document.toObject(Question::class.java).apply {
                        id = document.id // Add the document ID for navigation
                    }
                    questions.add(question)
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Questions") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues)
        ) {
            LazyColumn {
                items(questions) { question ->
                    QuestionItem(
                        question = question,
                        onClick = {
                            // Navigate to the QuestionDetailScreen with the question ID
                            navController.navigate("questionDetail/${question.id}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun QuestionItem(question: Question, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick) // Navigate when the card is clicked
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = question.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = question.description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyQuestionsScreenPreview() {
    // Preview MyQuestionsScreen
    MyQuestionsScreen(navController = NavHostController(LocalContext.current), userId = "sampleUserId")
}
