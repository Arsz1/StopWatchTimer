package com.example.timer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timer.domain.FormatTimeUseCase
import com.example.timer.domain.LapTime
import com.example.timer.domain.StopwatchState
import com.example.timer.service.TimerService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing stopwatch UI state and interactions.
 * Handles elapsed time tracking, lap recording, and state management.
 */
open class StopwatchViewModel(
    private val timerService: TimerService,
    private val formatTimeUseCase: FormatTimeUseCase
) : ViewModel() {

    private val _stopwatchState = MutableStateFlow<StopwatchState>(StopwatchState.Idle)
    val stopwatchState: StateFlow<StopwatchState> = _stopwatchState.asStateFlow()

    private val _formattedTime = MutableStateFlow("00:00.00")
    val formattedTime: StateFlow<String> = _formattedTime.asStateFlow()

    private val _lapTimes = MutableStateFlow<List<LapTime>>(emptyList())
    val lapTimes: StateFlow<List<LapTime>> = _lapTimes.asStateFlow()

    init {
        observeStopwatchState()
    }

    private fun observeStopwatchState() {
        viewModelScope.launch {
            timerService.observeStopwatchState().collect { state ->
                _stopwatchState.value = state
                updateFormattedTime(state)
                updateLapTimes(state)
            }
        }
    }

    private fun updateFormattedTime(state: StopwatchState) {
        val timeString = when (state) {
            is StopwatchState.Idle -> "00:00.00"
            is StopwatchState.Running -> formatTimeUseCase.formatStopwatchTime(state.elapsedMillis)
            is StopwatchState.Paused -> formatTimeUseCase.formatStopwatchTime(state.elapsedMillis)
        }
        _formattedTime.value = timeString
    }

    private fun updateLapTimes(state: StopwatchState) {
        val laps = when (state) {
            is StopwatchState.Idle -> emptyList()
            is StopwatchState.Running -> state.lapTimes
            is StopwatchState.Paused -> state.lapTimes
        }
        _lapTimes.value = laps
    }

    fun startStopwatch() {
        viewModelScope.launch {
            timerService.startStopwatch()
        }
    }

    fun pauseStopwatch() {
        viewModelScope.launch {
            timerService.pauseStopwatch()
        }
    }

    fun recordLap() {
        viewModelScope.launch {
            timerService.recordLap()
        }
    }

    fun resetStopwatch() {
        viewModelScope.launch {
            timerService.resetStopwatch()
        }
    }

    val isRunning: Boolean
        get() = _stopwatchState.value is StopwatchState.Running

    val isPaused: Boolean
        get() = _stopwatchState.value is StopwatchState.Paused
}
