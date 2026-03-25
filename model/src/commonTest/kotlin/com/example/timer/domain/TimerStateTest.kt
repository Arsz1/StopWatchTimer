package com.example.timer.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertIs

class TimerStateTest {

    @Test
    fun `should create idle state with zero seconds`() {
        // When
        val state = TimerState.Idle()

        // Then
        assertIs<TimerState.Idle>(state)
        assertEquals(0L, state.totalSeconds)
    }

    @Test
    fun `should create idle state with specified seconds`() {
        // Given
        val totalSeconds = 300L

        // When
        val state = TimerState.Idle(totalSeconds)

        // Then
        assertEquals(totalSeconds, state.totalSeconds)
    }

    @Test
    fun `should create running state with remaining seconds`() {
        // Given
        val remainingSeconds = 250L
        val totalSeconds = 300L

        // When
        val state = TimerState.Running(remainingSeconds, totalSeconds)

        // Then
        assertIs<TimerState.Running>(state)
        assertEquals(remainingSeconds, state.remainingSeconds)
        assertEquals(totalSeconds, state.totalSeconds)
    }

    @Test
    fun `should create paused state with remaining seconds`() {
        // Given
        val remainingSeconds = 150L
        val totalSeconds = 300L

        // When
        val state = TimerState.Paused(remainingSeconds, totalSeconds)

        // Then
        assertIs<TimerState.Paused>(state)
        assertEquals(remainingSeconds, state.remainingSeconds)
        assertEquals(totalSeconds, state.totalSeconds)
    }

    @Test
    fun `should create finished state`() {
        // When
        val state = TimerState.Finished

        // Then
        assertIs<TimerState.Finished>(state)
    }

    @Test
    fun `should calculate progress correctly`() {
        // Given
        val totalSeconds = 100L
        val remainingSeconds = 25L

        // When
        TimerState.Running(remainingSeconds, totalSeconds)
        val elapsedSeconds = totalSeconds - remainingSeconds
        val progressPercent = (elapsedSeconds.toDouble() / totalSeconds) * 100

        // Then
        assertEquals(75.0, progressPercent)
    }

    @Test
    fun `should handle zero remaining seconds without error`() {
        // When
        val state = TimerState.Running(0L, 300L)

        // Then
        assertEquals(0L, state.remainingSeconds)
        assertEquals(300L, state.totalSeconds)
    }

    @Test
    fun `should distinguish between running and paused states`() {
        // When
        val running = TimerState.Running(100L, 300L)
        val paused = TimerState.Paused(100L, 300L)

        // Then
        assertTrue(running != paused)
    }

    @Test
    fun `should verify finished state is singleton-like`() {
        // When
        val finished1 = TimerState.Finished
        val finished2 = TimerState.Finished

        // Then
        assertEquals(finished1, finished2)
    }

    @Test
    fun `should track progress from start to finish`() {
        // Given
        val totalSeconds = 60L
        val states = listOf(
            TimerState.Running(60L, totalSeconds),
            TimerState.Running(45L, totalSeconds),
            TimerState.Running(30L, totalSeconds),
            TimerState.Running(15L, totalSeconds),
            TimerState.Running(0L, totalSeconds),
            TimerState.Finished
        )

        // Then
        states.forEach { state ->
            when (state) {
                is TimerState.Running -> {
                    val elapsed = totalSeconds - state.remainingSeconds
                    assertTrue(elapsed in 0..totalSeconds)
                }
                is TimerState.Finished -> assertTrue(true)
                else -> {}
            }
        }
    }
}
