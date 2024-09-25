package com.lovegames.thirtysixforlove.ui

sealed class ThirtySixQuestionsState {
    data class Content(
        val currentQuestionIndex: Int = 0,
        val symmetry: Boolean = false,
        val playerTurnTimerCount: Int = 0,
        val showTimer: Boolean = true,
        val timerCompleted: Boolean = false,
        val hasPlayedSound: Boolean = false,
        val rotateTimer: Boolean = false
    ) : ThirtySixQuestionsState()
}