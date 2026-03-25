package com.example.timer.di

import com.example.timer.presentation.TimerViewModel
import com.example.timer.presentation.StopwatchViewModel
import com.example.timer.service.IosTimerService
import com.example.timer.domain.FormatTimeUseCase

/**
 * Dependency injection module for iOS platform.
 * Provides iOS-specific implementations and ViewModels.
 */
object IosModule {

    private var timerViewModelInstance: TimerViewModel? = null
    private var stopwatchViewModelInstance: StopwatchViewModel? = null

    /**
     * Provides a singleton instance of TimerViewModel.
     */
    fun provideTimerViewModel(): TimerViewModel {
        return timerViewModelInstance ?: run {
            val timerService = IosTimerService()
            val formatTimeUseCase = FormatTimeUseCase()

            TimerViewModel(
                timerService = timerService,
                formatTimeUseCase = formatTimeUseCase
            ).also { timerViewModelInstance = it }
        }
    }

    /**
     * Provides a singleton instance of StopwatchViewModel.
     */
    fun provideStopwatchViewModel(): StopwatchViewModel {
        return stopwatchViewModelInstance ?: run {
            val timerService = IosTimerService()
            val formatTimeUseCase = FormatTimeUseCase()

            StopwatchViewModel(
                timerService = timerService,
                formatTimeUseCase = formatTimeUseCase
            ).also { stopwatchViewModelInstance = it }
        }
    }

}