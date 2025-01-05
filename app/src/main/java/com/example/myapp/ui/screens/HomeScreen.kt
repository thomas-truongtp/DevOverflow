package com.example.myapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val allQuestions = remember { mutableStateListOf<Map<String, Any>>() }
    val displayedQuestions = remember { mutableStateListOf<Map<String, Any>>() }
    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        // Fetch questions from Firestore
        db.collection("questions").addSnapshotListener { value, _ ->
            allQuestions.clear()
            value?.documents?.forEach { document ->
                val question = document.data ?: mapOf()
                allQuestions.add(question + ("id" to document.id)) // Add document ID for navigation
            }
            displayedQuestions.addAll(allQuestions) // Initialize displayed questions
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("addQuestion") }) {
                Icon(Icons.Default.Add, contentDescription = "Add Question")
            }
        },


        topBar = {
            TopAppBar(
                title = { Text("Home") },
                actions = {
                    IconButton(onClick = { navController.navigate("myQuestions") }) {
                        Icon(Icons.Default.Person, contentDescription = "My Questions")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchText,
                onValueChange = { query ->
                    searchText = query
                    displayedQuestions.clear()
                    if (query.isEmpty()) {
                        displayedQuestions.addAll(allQuestions)
                    } else {
                        displayedQuestions.addAll(
                            allQuestions.filter { question ->
                                question["title"].toString().contains(query, ignoreCase = true) ||
                                        question["description"].toString().contains(query, ignoreCase = true)
                            }
                        )
                    }
                },
                label = { Text("Search Topics") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            // List of Questions
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(displayedQuestions) { question ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        onClick = { navController.navigate("questionDetail/${question["id"]}") }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = question["title"].toString(),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = question["description"].toString(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}


