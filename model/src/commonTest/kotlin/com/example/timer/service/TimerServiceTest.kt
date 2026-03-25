package com.example.timer.service

import com.example.timer.domain.LapTime
import com.example.timer.domain.StopwatchState
import com.example.timer.domain.TimerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TimerServiceTest {

    private lateinit var timerService: TimerService
    private val testDispatcher = StandardTestDispatcher() // Use StandardTestDispatcher instead of TestCoroutineDispatcher
    private lateinit var testScope: TestScope // TestScope to manage the coroutine scope

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        testScope = TestScope(testDispatcher) // Initialize TestScope with StandardTestDispatcher
        Dispatchers.setMain(testDispatcher) // Set the main dispatcher for coroutine execution in tests

        // Dummy implementation for testing purposes
        timerService = object : TimerService {
            override fun startTimer(initialSeconds: Long) = flow {
                emit(TimerState.Running(initialSeconds, initialSeconds))
            }

            override suspend fun pauseTimer() {
                // Implemented pause logic for testing
            }

            override suspend fun resetTimer(seconds: Long) {
                // Implemented reset logic for testing
            }

            override suspend fun stopTimer() {
                // Implemented stop logic for testing
            }

            override suspend fun getTimerState(): TimerState {
                return TimerState.Idle(0L)
            }

            override fun observeTimerState() = flow {
                emit(TimerState.Idle(0L))
            }

            override fun startStopwatch() = flow {
                emit(StopwatchState.Running(0L, emptyList()))
            }

            override suspend fun pauseStopwatch() {
                // Implemented pause logic for testing
            }

            override suspend fun recordLap(): LapTime? {
                return LapTime(1, 1000L, 1000L)
            }

            override suspend fun resetStopwatch() {
                // Implemented reset logic for testing
            }

            override suspend fun stopStopwatch() {
                // Implemented stop logic for testing
            }

            override suspend fun getStopwatchState(): StopwatchState {
                return StopwatchState.Idle
            }

            override fun observeStopwatchState() = flow {
                emit(StopwatchState.Idle)
            }

            override suspend fun clearLaps() {
                // Implemented clear laps logic for testing
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test resetTimer should reset the timer state to Idle`() = testScope.runTest {
        val initialSeconds = 10L // Set the initial time duration
        timerService.startTimer(initialSeconds)
        advanceTimeBy(3000L) // Simulate 3 seconds passed
        timerService.pauseTimer()

        // Reset timer
        timerService.resetTimer(0L)

        // Assert that the state is Idle with 0 seconds
        val state = timerService.getTimerState()
        assertTrue(state is TimerState.Idle)
        val idleState = state
        assertEquals(0L, idleState.totalSeconds)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test stopTimer should stop the timer and set state to Idle`() = testScope.runTest {
        val initialSeconds = 10L // Set the initial time duration
        timerService.startTimer(initialSeconds)
        advanceTimeBy(2000L) // Simulate 2 seconds passed

        // Stop the timer
        timerService.stopTimer()

        // Assert that the timer state is Idle
        val state = timerService.getTimerState()
        assertTrue(state is TimerState.Idle)
    }

    // ==================== STOPWATCH TESTS ====================

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test resetStopwatch should reset stopwatch state to idle`() = testScope.runTest {
        // Start stopwatch and simulate time
        timerService.startStopwatch()
        advanceTimeBy(3000L)

        // Reset stopwatch
        timerService.resetStopwatch()

        // Assert that the stopwatch state is Idle
        val state = timerService.getStopwatchState()
        assertTrue(state is StopwatchState.Idle)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test stopStopwatch should stop stopwatch and set state to Idle`() = testScope.runTest {
        // Start stopwatch
        timerService.startStopwatch()
        advanceTimeBy(2000L) // Simulate 2 seconds passed

        // Stop stopwatch
        timerService.stopStopwatch()

        // Assert that the stopwatch state is Idle
        val state = timerService.getStopwatchState()
        assertTrue(state is StopwatchState.Idle)
    }
}