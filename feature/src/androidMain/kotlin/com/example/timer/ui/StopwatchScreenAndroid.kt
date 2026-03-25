package com.example.timer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.timer.presentation.StopwatchViewModel
import com.example.timer.presentation.components.ButtonConfig
import com.example.timer.presentation.components.ControlButtons
import com.example.timer.presentation.components.LapsList
import com.example.timer.presentation.components.TimeDisplay
import org.koin.androidx.compose.koinViewModel

/**
 * Android-specific Stopwatch screen using Compose and Koin.
 * Injects the AndroidTimerService via the shared StopwatchViewModel.
 */
@Composable
fun StopwatchScreenAndroid(
    modifier: Modifier = Modifier,
    viewModel: StopwatchViewModel = koinViewModel(),
) {
    val formattedTime by viewModel.formattedTime.collectAsState()
    val lapTimes by viewModel.lapTimes.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Time Display
        TimeDisplay(
            time = formattedTime,
            modifier = Modifier.weight(0.35f)
        )

        // Control Buttons
        val buttons = buildStopwatchButtons(
            isRunning = viewModel.isRunning,
            isPaused = viewModel.isPaused,
            onStart = { viewModel.startStopwatch() },
            onPause = { viewModel.pauseStopwatch() },
            onLap = { viewModel.recordLap() },
            onResume = { viewModel.startStopwatch() },
            onReset = { viewModel.resetStopwatch() }
        )

        ControlButtons(buttons = buttons)

        // Laps List
        LapsList(
            laps = lapTimes,
            formatLapTime = { millis ->
                val totalSeconds = millis / 1000
                val millisRemainder = millis % 1000
                val minutes = totalSeconds / 60
                val seconds = totalSeconds % 60
                val minStr = minutes.toString().padStart(2, '0')
                val secStr = seconds.toString().padStart(2, '0')
                val msStr = (millisRemainder / 10).toString().padStart(2, '0')
                "$minStr:$secStr.$msStr"
            },
            modifier = Modifier.weight(0.65f)
        )
    }
}

private fun buildStopwatchButtons(
    isRunning: Boolean,
    isPaused: Boolean,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onLap: () -> Unit,
    onResume: () -> Unit,
    onReset: () -> Unit
): List<ButtonConfig> {
    return when {
        isRunning -> listOf(
            ButtonConfig(
                label = "Lap",
                onClick = onLap,
                color = Color(0xFF2196F3)
            ),
            ButtonConfig(
                label = "Pause",
                onClick = onPause,
                color = Color(0xFFFFA500)
            )
        )
        isPaused -> listOf(
            ButtonConfig(
                label = "Resume",
                onClick = onResume,
                color = Color(0xFF4CAF50)
            ),
            ButtonConfig(
                label = "Reset",
                onClick = onReset,
                color = Color(0xFFFF6B6B)
            )
        )
        else -> listOf(
            ButtonConfig(
                label = "Start",
                onClick = onStart,
                color = Color(0xFF4CAF50)
            )
        )
    }
}