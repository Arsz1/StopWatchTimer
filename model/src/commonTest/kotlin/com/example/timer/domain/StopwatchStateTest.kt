package com.example.timer.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertIs

class StopwatchStateTest {

    @Test
    fun `should create idle state`() {
        // When
        val state = StopwatchState.Idle

        // Then
        assertIs<StopwatchState.Idle>(state)
    }

    @Test
    fun `should create running state with elapsed time`() {
        // Given
        val elapsedMillis = 5000L

        // When
        val state = StopwatchState.Running(elapsedMillis)

        // Then
        assertIs<StopwatchState.Running>(state)
        assertEquals(elapsedMillis, state.elapsedMillis)
        assertEquals(emptyList(), state.lapTimes)
    }

    @Test
    fun `should create paused state with elapsed time`() {
        // Given
        val elapsedMillis = 5000L

        // When
        val state = StopwatchState.Paused(elapsedMillis)

        // Then
        assertIs<StopwatchState.Paused>(state)
        assertEquals(elapsedMillis, state.elapsedMillis)
        assertEquals(emptyList(), state.lapTimes)
    }

    @Test
    fun `should distinguish between running and paused states`() {
        // Given
        val elapsedMillis = 5000L

        // When
        val running = StopwatchState.Running(elapsedMillis)
        val paused = StopwatchState.Paused(elapsedMillis)

        // Then
        assertTrue(running != paused)
    }

    @Test
    fun `should verify idle state is singleton-like`() {
        // When
        val idle1 = StopwatchState.Idle
        val idle2 = StopwatchState.Idle

        // Then
        assertEquals(idle1, idle2)
    }
}
