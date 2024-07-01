package com.example.thritysixforlove.compose

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.thritysixforlove.ThirtySixQuestionsViewModelViewModel

@Composable
fun NavigationComponent(navController: NavHostController) {
    val thirtySixQuestionsViewModelViewModel: ThirtySixQuestionsViewModelViewModel = viewModel()
    NavHost(navController = navController, startDestination = "main_screen") {
        composable("main_screen") {
            MainScreen(navController)
        }
        composable("thirty_six_questions_screen") {
            ThirtySixQuestionsMainScreen(viewModel = thirtySixQuestionsViewModelViewModel)
        }
    }
}