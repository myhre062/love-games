package com.lovegames.thirtysixforlove.compose

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.lovegames.thirtysixforlove.ThirtySixQuestionsViewModelViewModel
import com.lovegames.thirtysixforlove.TimerCompletionAction
import com.lovegames.thirtysixforlove.ui.ThirtySixQuestionsState
import com.lovegames.thritysixforlove.R

@Composable
fun ThirtySixQuestionsMainScreen(
    viewModel: ThirtySixQuestionsViewModelViewModel,
    navController: NavController
) {
    val state = viewModel.state().collectAsState().value
    when (state) {
        is ThirtySixQuestionsState.Content -> {
            ThirtySixQuestionsMainScreenContent(
                state,
                viewModel,
                navController
            )
        }
    }
}

@Composable
private fun ThirtySixQuestionsMainScreenContent(
    state: ThirtySixQuestionsState.Content,
    viewModel: ThirtySixQuestionsViewModelViewModel,
    navController: NavController
) {
    val currentQuestionIndex = state.currentQuestionIndex
    val symmetry = state.symmetry
    val currentQuestionIndexValue = currentQuestionIndex + 1
    val isEvenQuestion = currentQuestionIndexValue % 2 == 0
    val isOddQuestion = currentQuestionIndexValue % 2 == 1
    val color = if (isEvenQuestion || state.playerTurnTimerCount == 1) Color.Magenta else Color.Red

    val animatedX by animateDpAsState(
        targetValue = when {
            !symmetry && (isEvenQuestion || state.playerTurnTimerCount == 1) -> 392.dp
            else -> 0.dp
        },
        animationSpec = tween(durationMillis = 600)
    )

    val animatedY by animateDpAsState(
        targetValue = when {
            symmetry && state.playerTurnTimerCount == 1 -> 0.dp
            symmetry && isOddQuestion || !symmetry -> 941.5.dp
            else -> 0.dp
        },
        animationSpec = tween(durationMillis = 600)
    )

    val snackbarHostState = remember { SnackbarHostState() }
    val triggerSnackbar = remember { mutableStateOf(false) }
    val keepTrack = stringResource(id = R.string.keep_track)

    val action = when {
        // If symmetry is true, rotate on first completion, dismiss on second completion.
        symmetry && state.playerTurnTimerCount == 1 -> TimerCompletionAction.RotateTimer
        symmetry && state.playerTurnTimerCount == 2 -> TimerCompletionAction.DismissTimer

        // If symmetry is false, do not rotate but dismiss on second completion.
        !symmetry && state.playerTurnTimerCount == 2 -> TimerCompletionAction.DismissTimer

        else -> TimerCompletionAction.DoNothing
    }

    LaunchedEffect(triggerSnackbar.value) {
        if (triggerSnackbar.value && currentQuestionIndex == 0) {
            snackbarHostState.showSnackbar(
                message = keepTrack,
                duration = SnackbarDuration.Short
            )
            triggerSnackbar.value = false // Reset the trigger
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    modifier = Modifier.padding(4.dp),
                    onClick = { viewModel.previousQuestion(navController) }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_back_arrow),
                        contentDescription = "Back"
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (symmetry) {
                            Spacer(modifier = Modifier.height(16.dp))

                            Question(
                                questionResId = viewModel.getCurrentQuestionResId(),
                                isUpsideDown = true
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                modifier = Modifier.rotate(180f),
                                text = stringResource(
                                    id = R.string.question_number,
                                    currentQuestionIndex + 1
                                ),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(128.dp))

                            if (currentQuestionIndexValue == 11 && action != TimerCompletionAction.DismissTimer) {
                                Timer(
                                    viewModel = viewModel,
                                    handleColor = Color.Red,
                                    inactiveBarColor = Color.Red,
                                    activeBarColor = Color.Magenta,
                                    modifier = Modifier.size(148.dp),
                                    action = action
                                )
                            }

                            Spacer(modifier = Modifier.height(128.dp))

                            Text(
                                text = stringResource(
                                    id = R.string.question_number,
                                    currentQuestionIndex + 1
                                ),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Question(
                                questionResId = viewModel.getCurrentQuestionResId(),
                                isUpsideDown = false
                            )
                        } else {
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = stringResource(
                                    id = R.string.question_number,
                                    currentQuestionIndex + 1
                                ),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Question(
                                questionResId = viewModel.getCurrentQuestionResId(),
                                isUpsideDown = false
                            )
                        }
                    }
                }

                IconButton(
                    modifier = Modifier.padding(4.dp),
                    onClick = { viewModel.nextQuestion(navController) }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_forward_arrow),
                        contentDescription = "Forward"
                    )
                }
            }
        }

        IconButton(
            modifier = Modifier
                .padding(vertical = 48.dp, horizontal = 16.dp)
                .align(Alignment.TopEnd),
            onClick = { viewModel.toggleSymmetry() },
        ) {
            val iconResId = if (symmetry) R.drawable.side_by_side else R.drawable.heart_symmetry
            Icon(
                modifier = Modifier.size(96.dp),
                painter = painterResource(id = iconResId),
                contentDescription = stringResource(id = R.string.toggle),
            )
        }

        Box(modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 240.dp)
        ) {
            // Show the Timer only on question 11 when symmetry is false
            if (
                !symmetry
                && action != TimerCompletionAction.DismissTimer
                && currentQuestionIndexValue == 11
            ) {
                Column {
                    Timer(
                        viewModel = viewModel,
                        handleColor = Color.Red,
                        inactiveBarColor = Color.Red,
                        activeBarColor = Color.Magenta,
                        modifier = Modifier.size(148.dp),
                        action = action
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .offset(x = animatedX, y = animatedY)
                .padding(16.dp)
                .size(24.dp)
                .background(color = color, shape = CircleShape)
                .then(
                    if (currentQuestionIndex == 0) {
                        Modifier.clickable {
                            triggerSnackbar.value = true // Set the trigger to launch the snackbar
                        }
                    } else Modifier
                )
        )

        CustomSnackbarHost(
            modifier = Modifier.align(Alignment.BottomCenter),
            snackbarHostState = snackbarHostState
        )
    }
}

@Composable
fun Question(
    questionResId: Int,
    isUpsideDown: Boolean = false
) {
    Text(
        text = stringResource(id = questionResId),
        modifier = Modifier.rotate(if (isUpsideDown) 180f else 0f),
        textAlign = TextAlign.Center
    )
}

@Composable
fun CustomSnackbarHost(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState
) {
    SnackbarHost(
        hostState = snackbarHostState,
        modifier = modifier.fillMaxWidth(), // Ensures the host takes full width
        snackbar = { data ->
            // Customizing the snackbar presentation
            Box {
                Snackbar(
                    snackbarData = data,
                    modifier = modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 40.dp)
                        .fillMaxWidth(0.5f) // Sets the snackbar to half width of its parent
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun QuestionScreenPreview() {
    ThirtySixQuestionsMainScreen(
        viewModel = ThirtySixQuestionsViewModelViewModel(),
        navController = rememberNavController()
    )
}