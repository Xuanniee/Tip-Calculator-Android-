package com.example.tipcalculator

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tipcalculator.ui.theme.TipCalculatorTheme

import org.junit.Test
import org.junit.Rule
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class TipCalculatorInstrumentedTestUI {
    @get: Rule
    val composeTestRule = createComposeRule()

    // Compiler knows @Test in androidTest refers to Instrumented Tests, while in Test Directory refers to Local Tests
    @Test
    fun calculate_20_percent_tip() {
        // Set the UI Content, Code looks similar to Main Activity SetContent where we render the Screen and App
        composeTestRule.setContent {
            TipCalculatorTheme {
                TipCalculatorScreen()
            }
        }

        // Accessing the UI Component as a Node to access its particular text with onNodeWithText() method to access TextField Composable
        composeTestRule.onNodeWithText("Bill Amount")
            .performTextInput("10")     // Pass in the Value of the Text that we wants to populate it with

        // Apply Same Approach for Tip Percentage
        composeTestRule.onNodeWithText("Tip (%)")
            .performTextInput("20")

        // Use Assertion to ensure that the Text Composable reflects the accurate Tip to be given
        composeTestRule.onNodeWithText("Tip Amount: $2.00").assertExists()
    }
}