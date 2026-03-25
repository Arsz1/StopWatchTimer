package com.example.timer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.timer.di.IosModule
import com.example.timer.presentation.TimerViewModel
import com.example.timer.presentation.StopwatchViewModel
import com.example.timer.ui.TimerScreenIos
import com.example.timer.ui.StopwatchScreenIos

/**
 * Main iOS application entry point.
 * Manages tab navigation between Timer and Stopwatch screens.
 */
@Composable
fun IosApp(
    timerViewModel: TimerViewModel = IosModule.provideTimerViewModel(),
    stopwatchViewModel: StopwatchViewModel = IosModule.provideStopwatchViewModel()
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Tab Navigation
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color(0xFFF5F5F5),
            contentColor = Color(0xFF1976D2)
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 },
                text = { Text("Timer") }
            )
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                text = { Text("Stopwatch") }
            )
        }

        // Screen Content
        when (selectedTabIndex) {
            0 -> TimerScreenIos(viewModel = timerViewModel)
            1 -> StopwatchScreenIos(viewModel = stopwatchViewModel)
        }
    }
}