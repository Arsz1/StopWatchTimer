package com.example.timer.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * Composable for control buttons (Play, Pause, Reset, Lap, etc.)
 */
@Composable
fun ControlButtons(
    buttons: List<ButtonConfig>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        buttons.forEachIndexed { index, button ->
            Button(
                onClick = button.onClick,
                enabled = button.enabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = button.color
                ),
                modifier = Modifier
            ) {
                if (button.icon != null) {
                    Icon(
                        imageVector = button.icon,
                        contentDescription = button.label,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(button.label)
            }

            if (index < buttons.size - 1) {
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
    }
}

data class ButtonConfig(
    val label: String,
    val onClick: () -> Unit,
    val enabled: Boolean = true,
    val color: Color = Color(0xFF6200EE),
    val icon: ImageVector? = null
)