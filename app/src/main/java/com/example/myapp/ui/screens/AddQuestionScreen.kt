package com.example.myapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapp.models.Question
import com.example.myapp.viewmodels.QuestionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddQuestionScreen(navController: NavHostController, userId: String) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Question") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Question Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth().height(150.dp),
                maxLines = 5
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (title.isNotEmpty() && description.isNotEmpty()) {
                        val question = Question(
                            title = title,
                            description = description,
                            authorId = userId, // Use the actual user ID here
                            timestamp = System.currentTimeMillis()
                        )
                        isSubmitting = true
                        // Add the question to the Firebase database
                        QuestionViewModel().addQuestion(question)
                        // Navigate back to the home screen after submitting
                        navController.navigate("home")
                    }
                },
                enabled = !isSubmitting,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("Submit Question")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddQuestionScreenPreview() {
    // Preview AddQuestionScreen
}
