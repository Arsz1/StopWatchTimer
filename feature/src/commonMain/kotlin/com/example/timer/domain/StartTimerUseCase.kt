package com.example.timer.domain

import com.example.timer.service.TimerService

/**
 * Use case for starting the countdown timer.
 */
class StartTimerUseCase(private val timerService: TimerService) {

    operator fun invoke(seconds: Long) {
        if (seconds <= 0) {
            throw IllegalArgumentException("Timer duration must be positive")
        }
        timerService.startTimer(seconds)
    }
}
