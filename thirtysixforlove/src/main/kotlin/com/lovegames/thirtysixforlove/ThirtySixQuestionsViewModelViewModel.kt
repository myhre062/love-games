package com.lovegames.thirtysixforlove

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.lovegames.thirtysixforlove.ui.ThirtySixQuestionsState
import com.lovegames.thritysixforlove.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ThirtySixQuestionsViewModelViewModel : ViewModel() {
    private val questions = listOf(
        R.string.question_1,
        R.string.question_2,
        R.string.question_3,
        R.string.question_4,
        R.string.question_5,
        R.string.question_6,
        R.string.question_7,
        R.string.question_8,
        R.string.question_9,
        R.string.question_10,
        R.string.question_11,
        R.string.question_12,
        R.string.question_13,
        R.string.question_14,
        R.string.question_15,
        R.string.question_16,
        R.string.question_17,
        R.string.question_18,
        R.string.question_19,
        R.string.question_20,
        R.string.question_21,
        R.string.question_22,
        R.string.question_23,
        R.string.question_24,
        R.string.question_25,
        R.string.question_26,
        R.string.question_27,
        R.string.question_28,
        R.string.question_29,
        R.string.question_30,
        R.string.question_31,
        R.string.question_32,
        R.string.question_33,
        R.string.question_34,
        R.string.question_35,
        R.string.question_36
    )

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex

    private val _symmetry = MutableStateFlow(false)

    val totalTime = 2400L // make sure this is 240000L before pushing code
    private val _currentTime = MutableStateFlow(totalTime)
    val currentTime: StateFlow<Long> = _currentTime

    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning

    private val _timerCompleted = MutableStateFlow(false)
    val timerCompleted: StateFlow<Boolean> = _timerCompleted

    var hasPlayedSound: Boolean = false

    var playerTurnTimerCount = 0

    private val _thirtySixQuestionsStateFlow = MutableStateFlow<ThirtySixQuestionsState>(
        ThirtySixQuestionsState.Content(
            currentQuestionIndex = _currentQuestionIndex.value,
            symmetry = _symmetry.value,
            currentTime = _currentTime.value,
            isTimerRunning = _isTimerRunning.value,
            timerCompleted = _timerCompleted.value,
            playerTurnTimerCount = playerTurnTimerCount
        )
    )

    fun state() = _thirtySixQuestionsStateFlow.asStateFlow()

    fun toggleTimer(action: TimerCompletionAction) {
        if (_currentTime.value <= 0) {
            _currentTime.value = totalTime
            _isTimerRunning.value = true
        } else {
            _isTimerRunning.value = !_isTimerRunning.value
        }
        if (_isTimerRunning.value) {
            startTimer(action)
        }
    }

    private fun startTimer(action: TimerCompletionAction) {
        viewModelScope.launch {
            while (_isTimerRunning.value && _currentTime.value > 0) {
                delay(100L)
                _currentTime.value -= 100L
                if (_currentTime.value <= 0) {
                    _isTimerRunning.value = false
                    _timerCompleted.value = true
                    hasPlayedSound = false
                }
                // In the viewmodel we are not accounting for the off by 1 error. 10 means 11th
                // question. Also only add when the timer reaches 0.
                if (currentQuestionIndex.value == 10 && _currentTime.value == 0L) playerTurnTimerCount++
            }

            onTimerCompleted(action)
        }
    }

    private fun onTimerCompleted(action: TimerCompletionAction) {
        when (action) {
            TimerCompletionAction.PlaySound -> playSound()
            TimerCompletionAction.RotateTimer -> rotateTimer()
            TimerCompletionAction.DismissTimer -> dismissTimer()
            else -> {}
        }
    }

    private fun playSound() {
        // Logic to play sound (implement your own method here)
        if (!hasPlayedSound) {
            hasPlayedSound = true
        }
    }

    private fun rotateTimer() {
        // Reset or adjust the timer state as necessary
    }

    private fun dismissTimer() {
        // Logic to dismiss timer
        resetTimer()
    }

    fun markSoundPlayed() { hasPlayedSound = true }

    fun toggleSymmetry() {
        _thirtySixQuestionsStateFlow.update {
            (it as ThirtySixQuestionsState.Content).copy(
                symmetry = !_symmetry.value
            )
        }
    }

    fun nextQuestion(navController: NavController) {
        _thirtySixQuestionsStateFlow.update { state ->
            if (state is ThirtySixQuestionsState.Content) {
                val nextIndex = state.currentQuestionIndex + 1
                if (nextIndex < questions.size) {
                    state.copy(currentQuestionIndex = nextIndex)
                } else {
                    resetTimer()
                    navController.navigate("thirty_six_questions_congratulations_screen")
                    state // return the unchanged state
                }
            } else {
                state // return the unchanged state
            }
        }
    }

    fun previousQuestion(navController: NavController) {
        _thirtySixQuestionsStateFlow.update { state ->
            if (state is ThirtySixQuestionsState.Content) {
                val prevIndex = state.currentQuestionIndex - 1
                if (prevIndex >= 0) {
                    state.copy(currentQuestionIndex = prevIndex)
                } else {
                    navController.navigate("thirty_six_questions_instructions_screen")
                    state // return the unchanged state
                }
            } else {
                state // return the unchanged state
            }
        }
    }



    fun getCurrentQuestionResId(): Int {
        val index = _thirtySixQuestionsStateFlow.value.let { state ->
            (state as ThirtySixQuestionsState.Content).currentQuestionIndex
        }
        return if (index in questions.indices) {
            questions[index]
        } else {
            R.string.question_1 // A fallback string in case of an invalid index
        }
    }

    fun resetViewModel() {
        viewModelScope.launch {
            resetTimer()
            _currentQuestionIndex.value = 0
            _symmetry.value = false
            playerTurnTimerCount = 0
        }
    }

    private fun resetTimer() {
        viewModelScope.launch {
            _currentTime.value = totalTime
            _isTimerRunning.value = false
            _timerCompleted.value = false
            hasPlayedSound = false
        }
    }
}