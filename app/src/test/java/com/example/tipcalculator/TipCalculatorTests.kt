package com.example.tipcalculator

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TipCalculatorTests {
    @Test
    fun calculate_20_percent_tip_no_roundup() {
        // Creating Constant Variables for Testing
        val amount = 10.0
        val tipPercent = 20.0
        val expectedTip = "$2.00"   // Since CalculateTip returns a formatted String value
        // Note that CalculateTip does not describe any Component, so it is not a Composable Function
        val actualTip = CalculateTip(amount = amount, roundUp = false, tipPercent = tipPercent)

        // Check if Method works correctly
        assertEquals(expectedTip, actualTip)
    }
}