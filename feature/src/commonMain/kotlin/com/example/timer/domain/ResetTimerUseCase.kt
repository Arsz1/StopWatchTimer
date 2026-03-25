package com.example.timer.domain

import com.example.timer.service.TimerService

/**
 * Use case for resetting the timer to initial state.
 */
class ResetTimerUseCase(private val timerService: TimerService) {

    suspend operator fun invoke(seconds: Long = 0L) {
        timerService.resetTimer(seconds)
    }
}
