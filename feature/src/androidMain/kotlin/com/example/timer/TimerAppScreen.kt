package com.example.timer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.timer.presentation.StopwatchViewModel
import com.example.timer.presentation.TimerViewModel
import com.example.timer.presentation.components.TabNavigation
import com.example.timer.ui.StopwatchScreenAndroid
import com.example.timer.ui.TimerScreenAndroid
import org.koin.androidx.compose.koinViewModel

@Composable
fun TimerAppScreen() {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Timer", "Stopwatch")

    Column(modifier = Modifier.fillMaxSize()) {
        // Tab Navigation
        TabNavigation(
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it },
            tabs = tabs,
            modifier = Modifier.padding(top = 50.dp)
        )

        // Tab Content
        Box(modifier = Modifier.fillMaxSize()) {
            when (selectedTabIndex) {
                0 -> {
                    val timerViewModel: TimerViewModel = koinViewModel()
                    TimerScreenAndroid(viewModel = timerViewModel)
                }
                1 -> {
                    val stopwatchViewModel: StopwatchViewModel = koinViewModel()
                    StopwatchScreenAndroid(viewModel = stopwatchViewModel)
                }
            }
        }
    }
}

@Composable
fun TimerAppTheme(content: @Composable () -> Unit) {
    // Basic Material3 theme setup
    content()
}