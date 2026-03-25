package com.example.timer.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LapTimeTest {

    @Test
    fun `should allow custom timestamp`() {
        // Given
        val customTimestamp = 1234567890L

        // When
        val lapTime = LapTime(
            lapNumber = 1,
            lapDurationMillis = 5000L,
            totalElapsedMillis = 5000L,
            timestamp = customTimestamp
        )

        // Then
        assertEquals(customTimestamp, lapTime.timestamp)
    }

    @Test
    fun `should correctly compare lap times for equality`() {
        // Given
        val lapTime1 = LapTime(1, 5000L, 5000L, 1000L)
        val lapTime2 = LapTime(1, 5000L, 5000L, 1000L)
        val lapTime3 = LapTime(1, 5000L, 5000L, 2000L)

        // Then
        assertEquals(lapTime1, lapTime2)
        assertTrue(lapTime1 != lapTime3)
    }
}
