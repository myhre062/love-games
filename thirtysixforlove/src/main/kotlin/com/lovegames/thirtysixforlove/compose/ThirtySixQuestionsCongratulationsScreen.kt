package com.lovegames.thirtysixforlove.compose

import android.media.MediaPlayer
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
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
import com.lovegames.moregames.MoreGamesButton
import com.lovegames.thirtysixforlove.ThirtySixQuestionsViewModelViewModel
import com.lovegames.thirtysixforlove.TimerCompletionAction
import com.lovegames.thirtysixforlove.ui.ThirtySixQuestionsState
import com.lovegames.thritysixforlove.R

@Composable
fun ThirtySixQuestionsCongratulationsScreen(
    viewModel: ThirtySixQuestionsViewModelViewModel,
    navController: NavController,
    showAd: () -> Unit
) {
    val state = viewModel.state().collectAsState().value

    when (state) {
        is ThirtySixQuestionsState.Content -> {
            ThirtySixQuestionsCongratulationsScreenContnent(
                viewModel,
                navController,
                state,
                showAd
            )
        }
    }
}

@Composable
private fun ThirtySixQuestionsCongratulationsScreenContnent(
    viewModel: ThirtySixQuestionsViewModelViewModel,
    navController: NavController,
    state: ThirtySixQuestionsState.Content,
    showAd: () -> Unit
) {
    val context = LocalContext.current
    val action = when {
        state.timerCompleted -> TimerCompletionAction.PlaySound
        else -> TimerCompletionAction.DoNothing
    }

    // Remember if the sound has been played, and preserve it across navigation
    if (state.timerCompleted && !state.hasPlayedSound) {
        val mediaPlayer = remember { MediaPlayer.create(context, R.raw.harp_strum) }
        DisposableEffect(Unit) {
            mediaPlayer.start()
            onDispose { mediaPlayer.release() }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
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
                modifier = Modifier.size(200.dp),
                action = action
            )

            Spacer(modifier = Modifier.height(32.dp))
        }

        Column(
            modifier = Modifier.alpha(if (state.timerCompleted) 1f else 0f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                onClick = {
                    if (state.timerCompleted) navController.navigate("thirty_six_questions_they_might_kiss_screen")
                }
            ) {
                Text(text = stringResource(R.string.thirty_six_questions_adventurous))
            }

            Spacer(modifier = Modifier.height(16.dp))

            MoreGamesButton(
                onClick = {
                    if (state.timerCompleted) {
                        viewModel.resetViewModel()
                        navController.navigate("main_screen")
                    }
                },
                showAd = showAd
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ThirtySixQuestionsCongratulationsScreenPreview() {
    ThirtySixQuestionsCongratulationsScreen(
        viewModel = ThirtySixQuestionsViewModelViewModel(),
        navController = NavController(LocalContext.current),
        showAd = {}
    )
}