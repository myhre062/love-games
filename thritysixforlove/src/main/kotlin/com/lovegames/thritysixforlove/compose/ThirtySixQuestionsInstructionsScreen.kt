package com.lovegames.thritysixforlove.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.lovegames.thritysixforlove.R


@Composable
fun ThirtySixQuestionsInstructionsScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.thirty_six_questions_instructions),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("thirty_six_questions_screen") }) {
            Text(text = stringResource(R.string.sounds_good))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ThirtySixQuestionsInstructionsScreenPreview() {
    ThirtySixQuestionsInstructionsScreen(navController = rememberNavController())
}