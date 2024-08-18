package com.lovegames.thirtysixforlove.compose

import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.lovegames.thirtysixforlove.ThirtySixQuestionsViewModelViewModel
import com.lovegames.thritysixforlove.R

@Composable
fun ThirtySixQuestionsCongratulationsScreen(
    viewModel: ThirtySixQuestionsViewModelViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val timerCompleted by viewModel.timerCompleted.collectAsState()
    val hasPlayedSound = viewModel.hasPlayedSound

    // Remember if the sound has been played, and preserve it across navigation
    var localHasPlayedSound by rememberSaveable { mutableStateOf(hasPlayedSound) }

    if (timerCompleted && !localHasPlayedSound) {
        val mediaPlayer = remember { MediaPlayer.create(context, R.raw.harp_strum) }
        DisposableEffect(Unit) {
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener {
                viewModel.markSoundPlayed() // Update ViewModel state
                localHasPlayedSound = true  // Update local state to prevent re-triggering
            }
            onDispose { mediaPlayer.release() }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.thirty_six_questions_congratulations),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.stare),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )

            Spacer(modifier = Modifier.height(64.dp))

            // Timer button with heart effects aka animations
            Timer(
                viewModel = viewModel,
                handleColor = Color.Red,
                inactiveBarColor = Color.Red,
                activeBarColor = Color.Magenta,
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))
        }

        Column(
            modifier = Modifier.alpha(if (timerCompleted) 1f else 0f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                onClick = {
                    if (timerCompleted) navController.navigate("thirty_six_questions_they_might_kiss_screen")
                }
            ) {
                Text(text = stringResource(R.string.thirty_six_questions_adventurous))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (timerCompleted) {
                        viewModel.resetViewModel()
                        navController.navigate("main_screen")
                    }
                }
            ) {
                Text(text = stringResource(R.string.thirty_six_questions_more_games))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ThirtySixQuestionsCongratulationsScreenPreview() {
    ThirtySixQuestionsCongratulationsScreen(
        viewModel = ThirtySixQuestionsViewModelViewModel(),
        navController = NavController(LocalContext.current)
    )
}