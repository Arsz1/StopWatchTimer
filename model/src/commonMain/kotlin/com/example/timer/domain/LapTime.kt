package com.example.timer.domain

/**
 * Platform-agnostic way to get current time in milliseconds.
 * Implementation varies by platform.
 */
internal expect fun getCurrentTimeMillis(): Long

/**
 * Data class representing a single lap time record.
 * @param lapNumber Sequential number of the lap (1-indexed)
 * @param lapDurationMillis Duration of this specific lap in milliseconds
 * @param totalElapsedMillis Total elapsed time up to this lap in milliseconds
 * @param timestamp Timestamp when the lap was recorded (milliseconds since epoch)
 */
data class LapTime(
    val lapNumber: Int,
    val lapDurationMillis: Long,
    val totalElapsedMillis: Long,
    val timestamp: Long = getCurrentTimeMillis()
)
