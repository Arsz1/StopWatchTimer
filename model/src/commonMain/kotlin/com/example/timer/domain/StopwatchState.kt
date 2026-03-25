package com.example.timer.domain

/**
 * Sealed class representing the state of the stopwatch.
 * Manages elapsed time and lap recording state.
 */
sealed class StopwatchState {
    /**
     * Initial state: stopwatch is idle, no time elapsed.
     */
    data object Idle : StopwatchState()

    /**
     * Running state: stopwatch is actively counting up.
     * @param elapsedMillis Total milliseconds elapsed since start
     * @param lapTimes List of recorded lap times
     */
    data class Running(
        val elapsedMillis: Long,
        val lapTimes: List<LapTime> = emptyList()
    ) : StopwatchState()

    /**
     * Paused state: stopwatch is temporarily stopped.
     * @param elapsedMillis Total milliseconds elapsed when paused
     * @param lapTimes List of recorded lap times
     */
    data class Paused(
        val elapsedMillis: Long,
        val lapTimes: List<LapTime> = emptyList()
    ) : StopwatchState()
}
