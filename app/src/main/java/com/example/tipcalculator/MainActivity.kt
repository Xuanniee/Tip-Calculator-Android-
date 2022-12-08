package com.example.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tipcalculator.ui.theme.TipCalculatorTheme
import java.text.NumberFormat
import kotlin.math.round

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipCalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TipCalculatorScreen()
                }
            }
        }
    }
}

@Composable
fun TipCalculatorScreen() {
    // Mutable State that receives 0 as a parameter wrapped in a State Object, making its value observable
    var amountInput by remember {
        // Importing remember setter and getter functions allows us to read and set amountInput
        mutableStateOf("")
    }

    // Mutable State for Tip
    var tipInput by remember {
        mutableStateOf("")
    }

    // Variable to remember State of the Switch
    var roundUp by remember {
        mutableStateOf(false)
    }

    // Interface to Control Focus in Compose
    val focusManager = LocalFocusManager.current

    // Convert to Double or a Null. If Null, return 0 after the Elvis Operator
    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val tipPercentage = tipInput.toDoubleOrNull() ?: 0.0
    
    // Calculate Tip
    val tip = CalculateTip(amount = amount, tipPercent = tipPercentage, roundUp = roundUp)

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(32.dp)
    ) {
        // Screen Title
        Text(
            text = stringResource(id = R.string.calculate_tip),
            fontSize = 24.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))
        // Text Field for User
        // Pass the Hoisted State back into the Child Function
        EditNumberField(
            value = amountInput,
            onValueChange = { amountInput = it },
            label = R.string.bill_amount,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(
                // Move Focus downwards to the next composable when the Next Button is clicked
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )
        )

        // Input Field for Tip Percentage
        EditNumberField(
            label = R.string.how_was_the_service,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                // Closes the Keyboard when Done is pressed
                onDone = { focusManager.clearFocus() }
            ),
            value = tipInput,
            onValueChange = { tipInput = it }
        )

        // Rounding Function
        RoundTipRow(
            // Setting Initial State
            roundUp = roundUp,
            // Updating the State when the Switch is clicked
            onRoundUpChanged = { roundUp = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Display the Tip Amount to be given
        Text(
            // Can use tip to sub into placeholder as the String has a %s placeholder
            text = stringResource(id = R.string.tip_amount, tip),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
    }

}

@Composable
fun EditNumberField(
    // Hoist the State by introducing 2 Parameters
    @StringRes label: Int,      // To indicate that it is meant to be a String Resource
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    value: String,
    onValueChange: (String) -> Unit,     // Takes a string as input but has no output
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,     // Set to Empty String; Since TextBox that displays the Value
        onValueChange = onValueChange, // Set to Empty Lambda Function; Callback that is triggered when User enters text
        label = { Text(text = stringResource(label))}, // Using Label instead of Hardcoding
        modifier = Modifier
            .fillMaxWidth(),
        singleLine = true,       // Ensures text box is a single horizontal textbox that is scrollable
        keyboardOptions = keyboardOptions, // Changing the look of the keyboard
        keyboardActions = keyboardActions   // Functionality for the Action Buttons i.e. Next/Done
    )
}

// Rounding Tip Switch Function
@Composable
private fun RoundTipRow(
    modifier: Modifier = Modifier,
    // Allowing us to hoist the state of the switch
    roundUp: Boolean,
    onRoundUpChanged: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .size(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Text for Rounding Tip
        Text(text = stringResource(id = R.string.round_up_tip))
        
        Switch(
            // Determines whether the Switch is Checked, i.e. the Current State
            checked = roundUp,
            // Callback called when the Switch is clicked
            onCheckedChange = onRoundUpChanged,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End),
            colors = SwitchDefaults.colors(
                // Bad Practice since we are hardcoding the color and will be affected if Dark Mode is implemented for example
                uncheckedThumbColor = Color.DarkGray
            )
        )

    }
    
}

// Calculate Tip; Cannot be Private or the Local Tests will not have access to them
@VisibleForTesting      // Makes the Function Public but only for Testing purposes
internal fun CalculateTip(
    amount: Double,
    tipPercent: Double = 15.0,
    roundUp: Boolean
): String {
    var tip = tipPercent / 100 * amount

    if (roundUp == true) {
        // Rounding Up
        tip = kotlin.math.ceil(tip)
    }
    // After calculating the tip, format and display the tip with the Number Class
    return NumberFormat.getCurrencyInstance().format(tip)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TipCalculatorTheme {
        TipCalculatorScreen()
    }
}