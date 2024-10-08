package com.example.lovegames.compose

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lovegames.thirtysixforlove.ThirtySixQuestionsViewModelViewModel
import com.lovegames.thirtysixforlove.compose.ThirtySixQuestionsCongratulationsScreen
import com.lovegames.thirtysixforlove.compose.ThirtySixQuestionsDisclaimerScreen
import com.lovegames.thirtysixforlove.compose.ThirtySixQuestionsInstructionsScreen
import com.lovegames.thirtysixforlove.compose.ThirtySixQuestionsMainScreen
import com.lovegames.thirtysixforlove.compose.ThirtySixQuestionsTheyMightKissScreen


@Composable
fun NavigationComponent(navController: NavHostController) {
    val thirtySixQuestionsViewModelViewModel: ThirtySixQuestionsViewModelViewModel = viewModel()
    NavHost(navController = navController, startDestination = "main_screen") {
        composable("main_screen") {
            MainScreen(navController)
        }
        composable("thirty_six_questions_disclaimer_screen") {
            ThirtySixQuestionsDisclaimerScreen(navController)
        }
        composable("thirty_six_questions_instructions_screen") {
            ThirtySixQuestionsInstructionsScreen(navController)
        }
        composable("thirty_six_questions_screen") {
            ThirtySixQuestionsMainScreen(
                viewModel = thirtySixQuestionsViewModelViewModel,
                navController = navController
            )
        }
        composable("thirty_six_questions_congratulations_screen") {
            ThirtySixQuestionsCongratulationsScreen(thirtySixQuestionsViewModelViewModel, navController)
        }
        composable("thirty_six_questions_they_might_kiss_screen") {
            ThirtySixQuestionsTheyMightKissScreen(thirtySixQuestionsViewModelViewModel,navController)
        }
    }
}