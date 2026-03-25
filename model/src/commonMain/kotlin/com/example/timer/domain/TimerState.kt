package com.example.timer.domain

/**
 * Sealed class representing the state of the countdown timer.
 * Ensures type-safe state management and prevents invalid state transitions.
 */
sealed class TimerState {
    /**
     * Initial state: timer is idle, waiting for user input.
     * @param totalSeconds Total seconds set by the user
     */
    data class Idle(val totalSeconds: Long = 0L) : TimerState()

    /**
     * Running state: timer is actively counting down.
     * @param remainingSeconds Seconds left until timer reaches zero
     * @param totalSeconds Original total seconds for progress calculation
     */
    data class Running(val remainingSeconds: Long, val totalSeconds: Long) : TimerState()

    /**
     * Paused state: timer is temporarily stopped but can be resumed.
     * @param remainingSeconds Seconds left when paused
     * @param totalSeconds Original total seconds for progress calculation
     */
    data class Paused(val remainingSeconds: Long, val totalSeconds: Long) : TimerState()

    /**
     * Finished state: timer has reached zero and completed.
     */
    data object Finished : TimerState()
}
