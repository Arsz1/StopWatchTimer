package com.example.timer.service

import com.example.timer.domain.TimerState
import com.example.timer.domain.StopwatchState
import com.example.timer.domain.LapTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

/**
 * iOS implementation of TimerService using coroutines.
 *
 * iOS-specific considerations:
 * - Uses platform.Foundation.NSDate for high-precision timing
 * - Respects iOS background task limitations
 * - Integrates with iOS lifecycle management
 * - Similar coroutine structure to Android for consistency
 */
class IosTimerService : TimerService {

    // Coroutine scope with SupervisorJob for resilience
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    // Timer state management
    private val _timerState = MutableStateFlow<TimerState>(TimerState.Idle())
    private val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    // Stopwatch state management
    private val _stopwatchState = MutableStateFlow<StopwatchState>(StopwatchState.Idle)
    private val stopwatchState: StateFlow<StopwatchState> = _stopwatchState.asStateFlow()

    // Job tracking for cancellation
    private var timerJob: Job? = null
    private var stopwatchJob: Job? = null

    // Stopwatch tracking
    private var stopwatchStartTime: Long = 0L
    private var stopwatchPausedTime: Long = 0L
    private var currentLapTimes: List<LapTime> = emptyList()
    private var lapCounter: Int = 0

    // ==================== TIMER OPERATIONS ====================

    override fun startTimer(initialSeconds: Long): Flow<TimerState> {
        // Cancel any existing timer job
        timerJob?.cancel()

        // Determine remaining time based on current state
        val remainingSeconds = when (val current = _timerState.value) {
            is TimerState.Paused -> current.remainingSeconds
            is TimerState.Running -> current.remainingSeconds
            else -> initialSeconds
        }

        timerJob = scope.launch {
            _timerState.value = TimerState.Running(remainingSeconds, initialSeconds)

            var timeLeft = remainingSeconds
            while (timeLeft > 0) {
                delay(1000L) // Delay for 1 second
                timeLeft--
                _timerState.value = TimerState.Running(timeLeft, initialSeconds)
            }

            // Timer finished
            _timerState.value = TimerState.Finished
        }

        return timerState
    }

    override suspend fun pauseTimer() {
        val current = _timerState.value
        if (current is TimerState.Running) {
            timerJob?.cancel()
            _timerState.value = TimerState.Paused(current.remainingSeconds, current.totalSeconds)
        }
    }

    override suspend fun resetTimer(seconds: Long) {
        timerJob?.cancel()
        _timerState.value = TimerState.Idle(seconds)
    }

    override suspend fun stopTimer() {
        timerJob?.cancel()
        _timerState.value = TimerState.Idle()
    }

    override suspend fun getTimerState(): TimerState {
        return _timerState.value
    }

    override fun observeTimerState(): Flow<TimerState> {
        return timerState
    }

    // ==================== STOPWATCH OPERATIONS ====================

    override fun startStopwatch(): Flow<StopwatchState> {
        // Cancel any existing stopwatch job
        stopwatchJob?.cancel()

        // Calculate the start time based on current state
        val baseElapsedMillis = when (val current = _stopwatchState.value) {
            is StopwatchState.Paused -> current.elapsedMillis
            is StopwatchState.Running -> current.elapsedMillis
            StopwatchState.Idle -> 0L
        }

        stopwatchStartTime = (NSDate().timeIntervalSince1970 * 1000).toLong()
        stopwatchPausedTime = baseElapsedMillis

        stopwatchJob = scope.launch {
            while (true) {
                delay(10L) // Update every 10ms for smooth UI

                val elapsed = stopwatchPausedTime +
                        ((NSDate().timeIntervalSince1970 * 1000).toLong() - stopwatchStartTime)

                _stopwatchState.value = StopwatchState.Running(
                    elapsedMillis = elapsed,
                    lapTimes = currentLapTimes
                )
            }
        }

        return stopwatchState
    }

    override suspend fun pauseStopwatch() {
        val current = _stopwatchState.value
        if (current is StopwatchState.Running) {
            stopwatchJob?.cancel()
            _stopwatchState.value = StopwatchState.Paused(
                elapsedMillis = current.elapsedMillis,
                lapTimes = current.lapTimes
            )
            stopwatchPausedTime = current.elapsedMillis
        }
    }

    override suspend fun recordLap(): LapTime? {
        val current = _stopwatchState.value
        if (current is StopwatchState.Running) {
            lapCounter++

            val lapDuration = if (currentLapTimes.isEmpty()) {
                current.elapsedMillis
            } else {
                current.elapsedMillis - currentLapTimes.last().totalElapsedMillis
            }

            val lapTime = LapTime(
                lapNumber = lapCounter,
                lapDurationMillis = lapDuration,
                totalElapsedMillis = current.elapsedMillis
            )

            currentLapTimes = currentLapTimes + lapTime

            // Update state with new lap
            _stopwatchState.value = StopwatchState.Running(
                elapsedMillis = current.elapsedMillis,
                lapTimes = currentLapTimes
            )

            return lapTime
        }
        return null
    }

    override suspend fun resetStopwatch() {
        stopwatchJob?.cancel()
        _stopwatchState.value = StopwatchState.Idle
        currentLapTimes = emptyList()
        lapCounter = 0
        stopwatchStartTime = 0L
        stopwatchPausedTime = 0L
    }

    override suspend fun stopStopwatch() {
        stopwatchJob?.cancel()
        _stopwatchState.value = StopwatchState.Idle
    }

    override suspend fun getStopwatchState(): StopwatchState {
        return _stopwatchState.value
    }

    override fun observeStopwatchState(): Flow<StopwatchState> {
        return stopwatchState
    }

    override suspend fun clearLaps() {
        currentLapTimes = emptyList()
        lapCounter = 0

        val current = _stopwatchState.value
        if (current is StopwatchState.Running) {
            _stopwatchState.value = StopwatchState.Running(
                elapsedMillis = current.elapsedMillis,
                lapTimes = emptyList()
            )
        } else if (current is StopwatchState.Paused) {
            _stopwatchState.value = StopwatchState.Paused(
                elapsedMillis = current.elapsedMillis,
                lapTimes = emptyList()
            )
        }
    }

}
