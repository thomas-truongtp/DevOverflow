package com.example.myapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapp.ui.screens.AddQuestionScreen
import com.example.myapp.ui.screens.HomeScreen
import com.example.myapp.ui.screens.LoginScreen
import com.example.myapp.ui.screens.MyQuestionsScreen
import com.example.myapp.ui.screens.QuestionDetailScreen
import com.example.myapp.ui.theme.MyAppTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            MyAppTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login") {
                LoginScreen(navController)
            }
            composable("home") {
                HomeScreen(navController)
            }
            composable("addQuestion") {
                val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
                AddQuestionScreen(navController, userId = userId)
            }
            composable("questionDetail/{questionId}") { backStackEntry ->
                val questionId = backStackEntry.arguments?.getString("questionId") ?: ""
                QuestionDetailScreen(navController, questionId)
            }
            composable("myQuestions") {
                val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

                MyQuestionsScreen(navController,userId)
            }
        }
    }
}
