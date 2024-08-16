package com.lovegames.thirtysixforlove

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.lovegames.thritysixforlove.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    val symmetry: StateFlow<Boolean> = _symmetry

    private val _totalTime = 240000L
    private val _currentTime = MutableStateFlow(_totalTime)
    val currentTime: StateFlow<Long> = _currentTime

    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning

    private val _timerCompleted = MutableStateFlow(false)
    val timerCompleted: StateFlow<Boolean> = _timerCompleted

    var hasPlayedSound: Boolean = false


    fun toggleTimer() {
        if (_currentTime.value <= 0) {
            _currentTime.value = _totalTime
            _isTimerRunning.value = true
        } else {
            _isTimerRunning.value = !_isTimerRunning.value
        }
        if (_isTimerRunning.value) {
            startTimer()
        }
    }

    private fun startTimer() {
        viewModelScope.launch {
            while (_isTimerRunning.value && _currentTime.value > 0) {
                delay(100L)
                _currentTime.value -= 100L
                if (_currentTime.value <= 0) {
                    _isTimerRunning.value = false
                    _timerCompleted.value = true
                    hasPlayedSound = false
                }
            }
        }
    }

    fun markSoundPlayed() {
        hasPlayedSound = true
    }

    fun toggleSymmetry() {
        viewModelScope.launch {
            _symmetry.value = !_symmetry.value
        }
    }

    fun nextQuestion(navController: NavController) {
        viewModelScope.launch {
            if (_currentQuestionIndex.value < questions.size - 1) {
                _currentQuestionIndex.value += 1
            } else {
                navController.navigate("thirty_six_questions_congratulations_screen")
            }
        }
    }

    fun previousQuestion(navController: NavController) {
        viewModelScope.launch {
            if (_currentQuestionIndex.value > 0) {
                _currentQuestionIndex.value -= 1
            } else {
                navController.navigate("thirty_six_questions_instructions_screen")
            }
        }
    }

    fun getCurrentQuestionResId(): Int {
        return questions[_currentQuestionIndex.value]
    }

    fun resetViewModel() {
        viewModelScope.launch {
            _currentTime.value = _totalTime
            _isTimerRunning.value = false
            _timerCompleted.value = false
            _currentQuestionIndex.value = 0
            _symmetry.value = false
            hasPlayedSound = false
        }
    }
}