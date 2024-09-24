package com.lovegames.thirtysixforlove.ui

sealed class ThirtySixQuestionsState {
    data class Content(
        val currentQuestionIndex: Int,
        val symmetry: Boolean,
        val playerTurnTimerCount: Int,
        val showTimer: Boolean = true,
        val timerCompleted: Boolean = false,
        val hasPlayedSound: Boolean = false,
        val rotateTimer: Boolean = false
    ) : ThirtySixQuestionsState()
}