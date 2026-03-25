import androidx.activity.ComponentActivity
import io.mockk.every
import io.mockk.mockk
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.timer.domain.FormatTimeUseCase
import com.example.timer.domain.TimerState
import com.example.timer.presentation.TimerViewModel
import com.example.timer.service.TimerService
import com.example.timer.ui.TimerScreenAndroid
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TimerAppScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    // Mock the dependencies
    private val timerService = mockk<TimerService>(relaxed = true)
    private val formatTimeUseCase = mockk<FormatTimeUseCase>(relaxed = true)

    // Create ViewModel with mocked dependencies
    private val timerViewModel = TimerViewModel(timerService, formatTimeUseCase)

    // Create state flows to control the ViewModel's behavior
    private val timerStateFlow = MutableStateFlow<TimerState>(TimerState.Idle(60))
    private val formattedTimeFlow = MutableStateFlow("01:00")

    init {
        // Mock the ViewModel's exposed flows
        every { timerViewModel.timerState } returns timerStateFlow
        every { timerViewModel.formattedTime } returns formattedTimeFlow
        every { timerViewModel.isRunning } returns true
        every { timerViewModel.isPaused } returns false
    }

    @Test
    fun shouldCountdownFromSetTime() {
        composeTestRule.setContent {
            TimerScreenAndroid(viewModel = timerViewModel)
        }

        composeTestRule.onNodeWithText("01:00").assertIsDisplayed()

        formattedTimeFlow.value = "00:59"
        timerStateFlow.value = TimerState.Running(59, 60)

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("00:59").assertIsDisplayed()
    }

    @Test
    fun shouldShowFinishedDialog() {
        composeTestRule.setContent {
            TimerScreenAndroid(viewModel = timerViewModel)
        }

        formattedTimeFlow.value = "00:00"
        timerStateFlow.value = TimerState.Finished

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Timer Finished").assertIsDisplayed()
        composeTestRule.onNodeWithText("Your timer has completed!").assertIsDisplayed()
    }
}