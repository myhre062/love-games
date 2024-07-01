package com.example.thritysixforlove.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thritysixforlove.R
import com.example.thritysixforlove.ThirtySixQuestionsViewModelViewModel

@Composable
fun ThirtySixQuestionsMainScreen(viewModel: ThirtySixQuestionsViewModelViewModel) {
    val currentQuestionIndex = viewModel.currentQuestionIndex.collectAsState()
    val symmetry = viewModel.symmetry.collectAsState()

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
            IconButton(onClick = { viewModel.previousQuestion() }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_back_arrow),
                    contentDescription = "Back"
                )
            }

            if (symmetry.value) {
                Question(
                    questionResId = viewModel.getCurrentQuestionResId(),
                    isUpsideDown = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    modifier = Modifier.graphicsLayer(scaleY = -1f),
                    text = stringResource(
                        id = R.string.question_number,
                        currentQuestionIndex.value + 1
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

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

            IconButton(onClick = { viewModel.nextQuestion() }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_forward_arrow),
                    contentDescription = "Forward"
                )
            }
        }



        Button(
            onClick = { viewModel.toggleSymmetry() },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Toggle Symmetry")
        }
    }
}

@Composable
fun Question(
    questionResId: Int,
    isUpsideDown: Boolean = false
) {
    Text(
        text = stringResource(id = questionResId),
        modifier = Modifier.graphicsLayer(scaleY = if (isUpsideDown) -1f else 1f),
        textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true)
@Composable
fun QuestionScreenPreview() {
    val viewModel = ThirtySixQuestionsViewModelViewModel()
    ThirtySixQuestionsMainScreen(viewModel = viewModel)
}
