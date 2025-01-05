package com.example.myapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AnswerInput(onSubmit: (String) -> Unit) {
    var answerText by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = answerText,
            onValueChange = { answerText = it },
            label = { Text("Your Answer") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            if (answerText.isNotEmpty()) {
                onSubmit(answerText)
                answerText = ""
            }
        }) {
            Text("Submit")
        }
    }
}
