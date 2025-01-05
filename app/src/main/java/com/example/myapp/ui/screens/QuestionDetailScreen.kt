package com.example.myapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionDetailScreen(navController: NavController, questionId: String) {
    val db = FirebaseFirestore.getInstance()
    val question = remember { mutableStateOf<Map<String, Any>?>(null) }
    val answers = remember { mutableStateListOf<Map<String, Any>>() }
    var newAnswer by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        // Fetch the question
        db.collection("questions").document(questionId).get()
            .addOnSuccessListener { document ->
                question.value = document.data
            }

        // Fetch answers
        db.collection("questions").document(questionId).collection("answers")
            .addSnapshotListener { value, _ ->
                answers.clear()
                value?.documents?.forEach { answers.add(it.data ?: mapOf()) }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Question Details") },
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
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            question.value?.let {
                Text(
                    text = it["title"].toString(),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it["description"].toString(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Answers", style = MaterialTheme.typography.titleMedium)
            LazyColumn(
                modifier = Modifier.fillMaxHeight(0.7f)
            ) {
                items(answers) { answer ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(
                            text = answer["answerText"].toString(),
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val answer = hashMapOf(
                        "answerText" to newAnswer,
                        "timestamp" to System.currentTimeMillis()
                    )
                    db.collection("questions").document(questionId).collection("answers")
                        .add(answer)
                    newAnswer = "" // Clear the input field
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Post Answer")
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = newAnswer,
                onValueChange = { newAnswer = it },
                label = { Text("Your Answer") },
                modifier = Modifier.fillMaxWidth()
            )


        }
    }
}
