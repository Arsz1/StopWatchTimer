package com.example.timer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.timer.domain.TimerState
import com.example.timer.presentation.components.ButtonConfig
import com.example.timer.presentation.components.ControlButtons
import com.example.timer.presentation.components.TimeDisplay
import com.example.timer.presentation.TimerViewModel

/**
 * iOS-specific implementation of the Timer Screen.
 * Optimized for iOS design patterns and safe area handling.
 */
@Composable
fun TimerScreenIos(
    viewModel: TimerViewModel,
    modifier: Modifier = Modifier
) {
    val formattedTime by viewModel.formattedTime.collectAsState()
    val progress by viewModel.progress.collectAsState()
    val timerState by viewModel.timerState.collectAsState()
    val inputMinutes by viewModel.inputMinutes.collectAsState()
    val inputSeconds by viewModel.inputSeconds.collectAsState()
    val isFinished by viewModel.isFinished.collectAsState()

    var showTimePicker by remember { mutableStateOf(true) }

    if (isFinished) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissFinishNotification() },
            title = { Text("Timer Finished") },
            text = { Text("Your timer has completed!") },
            confirmButton = {
                Button(onClick = { viewModel.dismissFinishNotification() }) {
                    Text("OK")
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 12.dp,
                bottom = 12.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        // Time Picker (shown when idle)
        if (timerState is TimerState.Idle && showTimePicker) {
            IosTimePickerSection(
                minutes = inputMinutes,
                seconds = inputSeconds,
                onMinutesChange = { viewModel.setInputTime(it, inputSeconds) },
                onSecondsChange = { viewModel.setInputTime(inputMinutes, it) }
            )
        }

        // Time Display with Progress
        TimeDisplay(
            time = formattedTime,
            progress = if (timerState is TimerState.Running || timerState is TimerState.Paused) progress else null,
            modifier = Modifier.height(280.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Control Buttons
        val buttons = buildTimerButtons(
            isRunning = viewModel.isRunning,
            isPaused = viewModel.isPaused,
            onStart = {
                showTimePicker = false
                viewModel.startTimer()
            },
            onPause = { viewModel.pauseTimer() },
            onResume = { viewModel.resumeTimer() },
            onReset = {
                showTimePicker = true
                viewModel.resetTimer()
            }
        )

        ControlButtons(buttons = buttons)
    }
}

@Composable
private fun IosTimePickerSection(
    minutes: Int,
    seconds: Int,
    onMinutesChange: (Int) -> Unit,
    onSecondsChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Set Timer Time",
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IosNumberPicker(
                value = minutes,
                onValueChange = onMinutesChange,
                range = 0..59
            )

            Text(
                text = " : ",
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            IosNumberPicker(
                value = seconds,
                onValueChange = onSecondsChange,
                range = 0..59
            )
        }
    }
}

@Composable
private fun IosNumberPicker(
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { onValueChange((value + 1).coerceIn(range)) },
            modifier = Modifier.padding(4.dp)
        ) {
            Text("▲", modifier = Modifier.padding(4.dp))
        }
        Text(
            text = value.toString().padStart(2, '0'),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Button(
            onClick = { onValueChange((value - 1).coerceIn(range)) },
            modifier = Modifier.padding(4.dp)
        ) {
            Text("▼", modifier = Modifier.padding(4.dp))
        }
    }
}

private fun buildTimerButtons(
    isRunning: Boolean,
    isPaused: Boolean,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onReset: () -> Unit
): List<ButtonConfig> {
    return when {
        isRunning -> listOf(
            ButtonConfig(
                label = "Pause",
                onClick = onPause,
                color = Color(0xFFFFA500)
            ),
            ButtonConfig(
                label = "Reset",
                onClick = onReset,
                color = Color(0xFFFF6B6B)
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