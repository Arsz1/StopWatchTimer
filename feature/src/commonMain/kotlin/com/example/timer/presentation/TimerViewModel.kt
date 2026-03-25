package com.example.timer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timer.domain.FormatTimeUseCase
import com.example.timer.domain.TimerState
import com.example.timer.service.TimerService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class TimerViewModel(
    private val timerService: TimerService,
    private val formatTimeUseCase: FormatTimeUseCase
) : ViewModel() {

    private val _timerState = MutableStateFlow<TimerState>(TimerState.Idle())
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    private val _formattedTime = MutableStateFlow("00:00")
    val formattedTime: StateFlow<String> = _formattedTime.asStateFlow()

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress.asStateFlow()

    private val _inputMinutes = MutableStateFlow(1)
    val inputMinutes: StateFlow<Int> = _inputMinutes.asStateFlow()

    private val _inputSeconds = MutableStateFlow(0)
    val inputSeconds: StateFlow<Int> = _inputSeconds.asStateFlow()

    private val _isFinished = MutableStateFlow(false)
    val isFinished: StateFlow<Boolean> = _isFinished.asStateFlow()

    init {
        observeTimer()
    }

    private fun observeTimer() {
        viewModelScope.launch {
            timerService.observeTimerState().collect { state ->
                _timerState.value = state
                _formattedTime.value = when (state) {
                    is TimerState.Idle -> formatTimeUseCase.formatTimerTime(state.totalSeconds)
                    is TimerState.Running -> formatTimeUseCase.formatTimerTime(state.remainingSeconds)
                    is TimerState.Paused -> formatTimeUseCase.formatTimerTime(state.remainingSeconds)
                    is TimerState.Finished -> "00:00"
                }
                _progress.value = when (state) {
                    is TimerState.Idle -> 0f
                    is TimerState.Running -> formatTimeUseCase.getTimerProgress(
                        state.remainingSeconds,
                        state.totalSeconds
                    )
                    is TimerState.Paused -> formatTimeUseCase.getTimerProgress(
                        state.remainingSeconds,
                        state.totalSeconds
                    )
                    is TimerState.Finished -> 1f
                }
                _isFinished.value = state is TimerState.Finished
            }
        }
    }

    fun setInputTime(minutes: Int, seconds: Int) {
        _inputMinutes.value = minutes.coerceIn(0, 59)
        _inputSeconds.value = seconds.coerceIn(0, 59)
    }

    fun startTimer() {
        viewModelScope.launch {
            val totalSeconds = (_inputMinutes.value * 60 + _inputSeconds.value).toLong()
            if (totalSeconds > 0) {
                timerService.startTimer(totalSeconds)
            }
        }
    }

    fun pauseTimer() {
        viewModelScope.launch {
            timerService.pauseTimer()
        }
    }

    fun resumeTimer() {
        viewModelScope.launch {
            val currentState = _timerState.value
            if (currentState is TimerState.Paused) {
                // Pass remainingSeconds to continue from paused point
                timerService.startTimer(currentState.remainingSeconds)
            }
        }
    }

    fun resetTimer() {
        viewModelScope.launch {
            val totalSeconds = (_inputMinutes.value * 60 + _inputSeconds.value).toLong()
            timerService.resetTimer(totalSeconds)
        }
    }

    fun dismissFinishNotification() {
        _isFinished.value = false
    }

    val isRunning: Boolean
        get() = _timerState.value is TimerState.Running

    val isPaused: Boolean
        get() = _timerState.value is TimerState.Paused
}