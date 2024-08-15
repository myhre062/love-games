package com.lovegames.thritysixforlove.compose

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.lovegames.thritysixforlove.R

@Composable
fun ThirtySixQuestionsDisclaimerScreen(navController: NavController) {
    val context = LocalContext.current

    val annotatedString = buildAnnotatedString {
        append(stringResource(R.string.the))
        append(" ")
        pushStringAnnotation(tag = "URL", annotation = "https://www.nytimes.com/2015/01/11/style/modern-love-to-fall-in-love-with-anyone-do-this.html")
        withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
            append(stringResource(R.string.new_york_times))
        }
        pop()
        append(" ")
        append(stringResource(R.string.thirty_six_questions_intro))
    }

    val disclaimerString = buildAnnotatedString {
        append(stringResource(R.string.thirty_six_questions_disclaimer))
        append(" ")
        append(stringResource(R.string.the))
        append(" ")
        pushStringAnnotation(tag = "URL", annotation = "https://36questionsinlove.com")
        withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
            append(stringResource(R.string.thirty_six_questions_website))
        }
        pop()
        append(" ")
        append(stringResource(R.string.thirty_six_question_inspired))
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // TODO: Add a Background image that needs to be developed during the UI stage

//        Image(
//            painter = painterResource(id = R.drawable.thirtysixquestionsdisclaimerbackground), // Replace with your image name
//            contentDescription = null,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier.fillMaxSize()
//        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(R.string.thirty_six_questions_title_36),
                style = MaterialTheme.typography.headlineLarge.copy(fontFamily = FontFamily.Cursive),
                color = Color.Red,
                fontSize = 44.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.thirty_six_questions_title_questions),
                style = MaterialTheme.typography.bodyLarge.copy(fontFamily = FontFamily.Cursive),
                color = Color.Red,
                fontSize = 44.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.thirty_six_questions_title_how_to_fall_in_love),
                style = MaterialTheme.typography.bodyLarge.copy(fontFamily = FontFamily.Cursive),
                fontSize = 44.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            ClickableText(
                text = annotatedString,
                onClick = { offset ->
                    annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                        .firstOrNull()?.let { annotation ->
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                            context.startActivity(intent)
                        }
                },
                modifier = Modifier.padding(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            ClickableText(
                text = disclaimerString,
                onClick = { offset ->
                    disclaimerString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                        .firstOrNull()?.let { annotation ->
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                            context.startActivity(intent)
                        }
                },
                modifier = Modifier.padding(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.navigate("thirty_six_questions_instructions_screen") }) {
                Text(text = stringResource(R.string.start_thirty_six_questions_game))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ThirtySixQuestionsDisclaimerScreenPreview() {
    ThirtySixQuestionsDisclaimerScreen(rememberNavController())
}