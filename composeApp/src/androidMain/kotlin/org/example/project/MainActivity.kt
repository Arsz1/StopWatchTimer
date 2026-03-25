package org.example.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.timer.TimerAppScreen
import com.example.timer.TimerAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimerAppTheme {
                TimerAppScreen()
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    TimerAppScreen()
}