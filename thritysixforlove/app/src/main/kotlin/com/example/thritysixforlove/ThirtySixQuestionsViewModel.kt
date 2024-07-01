package com.example.thritysixforlove

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ThirtySixQuestionsViewModelViewModel : ViewModel() {
    private val disclaimer = R.string.disclaimer
    private val instructions = R.string.instructions

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

    private val congratulations = R.string.congratulations
    private val stare = R.string.stare

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex

    private val _symmetry = MutableStateFlow(false)
    val symmetry: StateFlow<Boolean> = _symmetry

    fun toggleSymmetry() {
        viewModelScope.launch {
            _symmetry.value = !_symmetry.value
        }
    }

    fun nextQuestion() {
        viewModelScope.launch {
            if (_currentQuestionIndex.value < questions.size - 1) {
                _currentQuestionIndex.value += 1
            }
        }
    }

    fun previousQuestion() {
        viewModelScope.launch {
            if (_currentQuestionIndex.value > 0) {
                _currentQuestionIndex.value -= 1
            }
        }
    }

    fun getCurrentQuestionResId(): Int {
        return questions[_currentQuestionIndex.value]
    }
}
