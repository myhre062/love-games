package com.lovegames.thirtysixforlove.compose

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.lovegames.thritysixforlove.R


@Composable
fun ThirtySixQuestionsMainScreen(viewModel: ThirtySixQuestionsViewModelViewModel, navController: NavController) {
    val currentQuestionIndex = viewModel.currentQuestionIndex.collectAsState()
    val symmetry = viewModel.symmetry.collectAsState()
    val currentQuestionIndexValue = currentQuestionIndex.value + 1
    val isEvenQuestion = currentQuestionIndexValue % 2 == 0
    val isOddQuestion = currentQuestionIndexValue % 2 == 1
    val color = if (isEvenQuestion) Color.Magenta else Color.Red

    val animatedX by animateDpAsState(
        targetValue = when {
            !symmetry.value && isEvenQuestion -> 392.dp
            else -> 0.dp
        },
        animationSpec = tween(durationMillis = 600)
    )

    val animatedY by animateDpAsState(
        targetValue = when {
            symmetry.value && isOddQuestion || !symmetry.value -> 941.5.dp
            else -> 0.dp
        },
        animationSpec = tween(durationMillis = 600)
    )

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
                        Spacer(modifier = Modifier.height(16.dp))

                        if (symmetry.value) {
                            Question(
                                questionResId = viewModel.getCurrentQuestionResId(),
                                isUpsideDown = true
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                modifier = Modifier.rotate(180f),
                                text = stringResource(
                                    id = R.string.question_number,
                                    currentQuestionIndex.value + 1
                                ),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(256.dp))

                            Text(
                                text = stringResource(
                                    id = R.string.question_number,
                                    currentQuestionIndex.value + 1
                                ),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Question(
                                questionResId = viewModel.getCurrentQuestionResId(),
                                isUpsideDown = false
                            )
                        } else {
                            Text(
                                text = stringResource(
                                    id = R.string.question_number,
                                    currentQuestionIndex.value + 1
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
            Icon(
                painter = painterResource(id = R.drawable.heart_symmetry),
                contentDescription = stringResource(id = R.string.toggle),
            )
        }

        Box(
            modifier = Modifier
                .offset(x = animatedX, y = animatedY)
                .padding(16.dp)
                .size(24.dp)
                .background(color = color, shape = CircleShape)
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

@Preview(showBackground = true)
@Composable
fun QuestionScreenPreview() {
    ThirtySixQuestionsMainScreen(
        viewModel = ThirtySixQuestionsViewModelViewModel(),
        navController = rememberNavController()
    )
}