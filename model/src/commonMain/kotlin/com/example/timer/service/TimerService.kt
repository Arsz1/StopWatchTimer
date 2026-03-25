package com.example.timer.service

import com.example.timer.domain.TimerState
import com.example.timer.domain.StopwatchState
import com.example.timer.domain.LapTime
import kotlinx.coroutines.flow.Flow

/**
 * Service interface for managing timer and stopwatch operations.
 * Provides platform-agnostic abstraction for time-based operations.
 *
 * Implementations should handle:
 * - Precise timing using platform-specific APIs
 * - Coroutine lifecycle management
 * - State emission via Kotlin Flow
 * - Proper cleanup on cancellation
 */
interface TimerService {

    // ==================== TIMER OPERATIONS ====================

    /**
     * Starts or resumes the countdown timer.
     * @param initialSeconds Seconds to count down from (only used on first start)
     * @return Flow emitting TimerState updates
     */
    fun startTimer(initialSeconds: Long): Flow<TimerState>

    /**
     * Pauses the countdown timer.
     * Timer can be resumed with [startTimer] using the same remaining time.
     */
    suspend fun pauseTimer()

    /**
     * Resets the timer to idle state with specified duration.
     * @param seconds Duration to set for the next timer session
     */
    suspend fun resetTimer(seconds: Long)

    /**
     * Stops the timer completely and cleans up resources.
     */
    suspend fun stopTimer()

    /**
     * Gets the current state of the timer.
     * @return Current TimerState
     */
    suspend fun getTimerState(): TimerState

    /**
     * Observes timer state changes.
     * @return Flow emitting TimerState updates whenever state changes
     */
    fun observeTimerState(): Flow<TimerState>


    // ==================== STOPWATCH OPERATIONS ====================

    /**
     * Starts or resumes the stopwatch.
     * @return Flow emitting StopwatchState updates
     */
    fun startStopwatch(): Flow<StopwatchState>

    /**
     * Pauses the stopwatch.
     * Stopwatch can be resumed with [startStopwatch] from the same elapsed time.
     */
    suspend fun pauseStopwatch()

    /**
     * Records a lap time in the stopwatch.
     * Only works when stopwatch is running.
     * @return LapTime object representing the recorded lap
     */
    suspend fun recordLap(): LapTime?

    /**
     * Resets the stopwatch to idle state with no lap times.
     */
    suspend fun resetStopwatch()

    /**
     * Stops the stopwatch completely and cleans up resources.
     */
    suspend fun stopStopwatch()

    /**
     * Gets the current state of the stopwatch.
     * @return Current StopwatchState
     */
    suspend fun getStopwatchState(): StopwatchState

    /**
     * Observes stopwatch state changes.
     * @return Flow emitting StopwatchState updates whenever state changes
     */
    fun observeStopwatchState(): Flow<StopwatchState>

    /**
     * Clears all recorded lap times.
     */
    suspend fun clearLaps()
}
