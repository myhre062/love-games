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

    private val _thirtySixQuestionsStateFlow = MutableStateFlow<ThirtySixQuestionsState>(
        ThirtySixQuestionsState.Content()
    )

    val totalTime = 240000L // make sure this is 240000L (4 min) before pushing code
    private val _currentTime = MutableStateFlow(totalTime)
    val currentTime: StateFlow<Long> = _currentTime

    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning

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
            while (_isTimerRunning.value && _currentTime.value > 0L) {
                delay(100L)
                _currentTime.value = (_currentTime.value - 100L).coerceAtLeast(0L)
            }
            if (_currentTime.value == 0L) {
                _isTimerRunning.value = false
                // Increment playerTurnTimerCount
                _thirtySixQuestionsStateFlow.update { state ->
                    if (state is ThirtySixQuestionsState.Content) {
                        state.copy(
                            playerTurnTimerCount = state.playerTurnTimerCount + 1,
                            timerCompleted = state.currentQuestionIndex >= questions.size - 1
                        )
                    } else {
                        state
                    }
                }

                // Call the completion action
                onTimerCompleted(action)
            }
        }
    }



    private fun onTimerCompleted(action: TimerCompletionAction) {
        when (action) {
            TimerCompletionAction.PlaySound -> markSoundPlayed() // need to something here in the future
            TimerCompletionAction.RotateTimer -> rotateTimer()
            TimerCompletionAction.DismissTimer -> dismissTimer()
            else -> {}
        }
    }

    // Expose the state as a StateFlow
    fun state(): StateFlow<ThirtySixQuestionsState> = _thirtySixQuestionsStateFlow.asStateFlow()

    fun toggleSymmetry() {
        _thirtySixQuestionsStateFlow.update { state ->
            if (state is ThirtySixQuestionsState.Content) {
                state.copy(symmetry = !state.symmetry)
            } else {
                state
            }
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
                    state
                }
            } else {
                state
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
                    state
                }
            } else {
                state
            }
        }
    }

    fun markSoundPlayed() {
        _thirtySixQuestionsStateFlow.update { state ->
            if (state is ThirtySixQuestionsState.Content) {
                state.copy(hasPlayedSound = true)
            } else {
                state
            }
        }
    }

    private fun rotateTimer() {
        _thirtySixQuestionsStateFlow.update { state ->
            if (state is ThirtySixQuestionsState.Content) {
                state.copy(rotateTimer = true)
            } else {
                state
            }
        }
    }

    private fun dismissTimer() {
        _thirtySixQuestionsStateFlow.update { state ->
            if (state is ThirtySixQuestionsState.Content) {
                if (state.playerTurnTimerCount == 2) {
                    state.copy(showTimer = false)
                } else {
                    state
                }
            } else {
                state
            }
        }
    }

    fun getCurrentQuestionResId(): Int {
        val index = (_thirtySixQuestionsStateFlow.value as ThirtySixQuestionsState.Content).currentQuestionIndex
        return if (index in questions.indices) {
            questions[index]
        } else {
            R.string.question_1 // Fallback question
        }
    }

    // Resets the ViewModel to the initial state
    fun resetViewModel() {
        _thirtySixQuestionsStateFlow.update { state ->
            if (state is ThirtySixQuestionsState.Content) {
                state.copy(
                    currentQuestionIndex = 0,
                    symmetry = false,
                    playerTurnTimerCount = 0,
                    showTimer = true,
                    hasPlayedSound = false,
                    rotateTimer = false
                )
            } else {
                state
            }
        }
        resetTimer()
    }

    private fun resetTimer() {
        _currentTime.value = totalTime
        _isTimerRunning.value = false
        _thirtySixQuestionsStateFlow.update { state ->
            if (state is ThirtySixQuestionsState.Content) {
                state.copy(
                    playerTurnTimerCount = 0,
                    hasPlayedSound = false,
                    rotateTimer = false,
                    showTimer = true,
                    timerCompleted = false
                )
            } else {
                state
            }
        }
    }
}
