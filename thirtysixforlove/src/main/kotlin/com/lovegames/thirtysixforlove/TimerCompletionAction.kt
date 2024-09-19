package com.lovegames.thirtysixforlove

sealed interface TimerCompletionAction {
    data object PlaySound : TimerCompletionAction
    data object RotateTimer: TimerCompletionAction
    data object DismissTimer: TimerCompletionAction
    data object DoNothing: TimerCompletionAction
}