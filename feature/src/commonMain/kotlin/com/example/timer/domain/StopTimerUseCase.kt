package com.example.timer.domain

import com.example.timer.service.TimerService

/**
 * Use case for stopping/pausing the timer.
 */
class StopTimerUseCase(private val timerService: TimerService) {

    suspend operator fun invoke() {
        timerService.pauseTimer()
    }
}
