package com.lovegames.thirtysixforlove.ui

sealed class ThirtySixQuestionsState {
    data class Content(
        val currentQuestionIndex: Int,
        val symmetry: Boolean,
        val currentTime: Long,
        val isTimerRunning: Boolean,
        val timerCompleted: Boolean,
        val playerTurnTimerCount: Int
    ) : ThirtySixQuestionsState()
}