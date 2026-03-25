package com.example.timer.domain

/**
 * Use case for formatting time values into human-readable strings.
 * Supports both timer (MM:SS) and stopwatch (MM:SS.ms) formats.
 */
class FormatTimeUseCase {

    /**
     * Format seconds into MM:SS format (for timer)
     */
    fun formatTimerTime(seconds: Long): String {
        val minutes = seconds / 60
        val secs = seconds % 60
        val min = minutes.toString().padStart(2, '0')
        val sec = secs.toString().padStart(2, '0')
        return "$min:$sec"
    }

    /**
     * Format milliseconds into MM:SS.ms format (for stopwatch)
     */
    fun formatStopwatchTime(millis: Long): String {
        val totalSeconds = millis / 1000
        val millisRemainder = millis % 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60

        val minStr = minutes.toString().padStart(2, '0')
        val secStr = seconds.toString().padStart(2, '0')
        val msStr = (millisRemainder / 10).toString().padStart(2, '0')

        return "$minStr:$secStr.$msStr"
    }

    /**
     * Get progress percentage for timer (0-100)
     */
    fun getTimerProgress(remaining: Long, total: Long): Float {
        if (total == 0L) return 0f
        return ((total - remaining) / total.toFloat()).coerceIn(0f, 1f)
    }
}
