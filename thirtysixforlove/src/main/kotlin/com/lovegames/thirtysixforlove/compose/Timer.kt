package com.lovegames.thirtysixforlove.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lovegames.thirtysixforlove.ThirtySixQuestionsViewModelViewModel
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun Timer(
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
private fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%1d:%02d", minutes, remainingSeconds)
}