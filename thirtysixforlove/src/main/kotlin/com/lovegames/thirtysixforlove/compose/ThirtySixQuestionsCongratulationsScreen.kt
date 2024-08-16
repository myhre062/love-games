package com.lovegames.thirtysixforlove.compose

import android.media.MediaPlayer
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lovegames.thirtysixforlove.ThirtySixQuestionsViewModelViewModel
import com.lovegames.thritysixforlove.R
import kotlin.math.cos
import kotlin.math.sin

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

@Composable
private fun Timer(
    viewModel: ThirtySixQuestionsViewModelViewModel,
    handleColor: Color,
    inactiveBarColor: Color,
    activeBarColor: Color,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 4.dp
) {
    val currentTime by viewModel.currentTime.collectAsState()
    val isTimerRunning by viewModel.isTimerRunning.collectAsState()
    val totalTime = 240000L

    var size by remember { mutableStateOf(IntSize.Zero) }
    val value = currentTime / totalTime.toFloat()

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .onSizeChanged {
                size = it
            }
    ) {
        Canvas(modifier = modifier) {
            drawArc(
                color = inactiveBarColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )

            drawArc(
                color = activeBarColor,
                startAngle = -90f,
                sweepAngle = 360f * value,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }

        val beta = (360f * value - 90f) * (Math.PI / 180f).toFloat()
        val r = size.width / 2f
        val a = cos(beta) * r
        val b = sin(beta) * r

        // Draw the heart Icon
        if (isTimerRunning) {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .offset {
                        // Calculate the position based on the angle
                        val offsetX = (a).toInt()
                        val offsetY = (b).toInt()
                        IntOffset(offsetX, offsetY)
                    },
                imageVector = Icons.Default.Favorite,
                contentDescription = "Timer",
                tint = handleColor
            )
        }

        if (currentTime == 0L || currentTime == 240000L) {
            // Show heart Icon
            IconButton(
                onClick = { viewModel.toggleTimer() },
            ) {
                val iconSize = with(LocalDensity.current) { 44.sp.toDp() }

                Icon(
                    modifier = Modifier.size(iconSize),
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Timer",
                    tint = Color.Red
                )
            }
        } else {
            // Draw the timer text
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { viewModel.toggleTimer() },
                text = formatTime((currentTime / 1000L).toInt()),
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                color = if (isTimerRunning) Color.Red else Color.Magenta
            )
        }
    }
}

@Composable
fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%1d:%02d", minutes, remainingSeconds)
}

@Preview(showBackground = true)
@Composable
fun ThirtySixQuestionsCongratulationsScreenPreview() {
    ThirtySixQuestionsCongratulationsScreen(
        viewModel = ThirtySixQuestionsViewModelViewModel(),
        navController = NavController(LocalContext.current)
    )
}