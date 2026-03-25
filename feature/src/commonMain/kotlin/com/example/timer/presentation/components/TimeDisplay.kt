package com.example.timer.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Composable for displaying time with optional progress indicator.
 */
@Composable
fun TimeDisplay(
    time: String,
    modifier: Modifier = Modifier,
    progress: Float? = null,
    fontSize: Int = 64
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (progress != null) {
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.size(280.dp),
                strokeWidth = 8.dp
            )
        }

        Text(
            text = time,
            style = TextStyle(
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = if (progress != null) {
                Modifier.size(280.dp)
            } else {
                Modifier
            }
        )
    }
}
