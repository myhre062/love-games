package com.lovegames.thirtysixforlove.compose

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.lovegames.thirtysixforlove.R
import com.lovegames.thirtysixforlove.ThirtySixQuestionsViewModelViewModel

@Composable
fun ThirtySixQuestionsTheyMightKissScreen(
    viewModel: ThirtySixQuestionsViewModelViewModel,
    navController: NavController
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = stringResource(R.string.thirty_six_questions_they_might_kiss),
            fontSize = 24.sp,
            maxLines = 2
        )

        Spacer(modifier = Modifier.height(16.dp))

        val theyMightKissText = buildAnnotatedString {
            append(stringResource(R.string.thirty_six_questions_can_see))
            append(" ")
            pushStringAnnotation(tag = "URL", annotation = "https://youtu.be/Tvne48F0Eqw?t=314")
            withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                append(stringResource(R.string.thirty_six_questions_here))
            }
            pop()
            append(" ")
            append(stringResource(R.string.thirty_six_questions_maybe))
        }

        ClickableText(
            modifier = Modifier.padding(16.dp),
            text = theyMightKissText,
            onClick = { offset ->
                theyMightKissText.getStringAnnotations(tag = "URL", start = offset, end = offset)
                    .firstOrNull()?.let { annotation ->
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                        context.startActivity(intent)
                    }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.resetViewModel()
                navController.navigate("main_screen")
            }
        ) {
            Text(text = stringResource(R.string.thirty_six_questions_more_games))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ThirtySixQuestionsTheyMightKissScreenPreview() {
    ThirtySixQuestionsTheyMightKissScreen(
        viewModel = ThirtySixQuestionsViewModelViewModel(),
        navController = NavController(LocalContext.current)
    )
}