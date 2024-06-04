package net.mrmdi.pmp.bmi_kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.mrmdi.pmp.bmi_kotlin.ui.theme.BMIKotlinTheme

// keyboardOptions, keyboardType
//import androidx.compose.ui.text.input.
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType

// builder
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import net.mrmdi.pmp.bmi_kotlin.viewmodel.BMIViewModel
import net.mrmdi.pmp.bmi_kotlin.ui.screens.InputScreen
import net.mrmdi.pmp.bmi_kotlin.ui.screens.ResultScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: BMIViewModel by viewModels()

        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "inputScreen") {
                composable("inputScreen") { InputScreen(navController, viewModel) }
                composable("resultScreen") { ResultScreen(navController, viewModel) }
            }
        }
    }
}




@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BMICalculator() {
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("") }
    var resultColor by remember { mutableStateOf(Color.Black) }

    var snackbarHostState = remember { SnackbarHostState() }

    val coroutineScope = rememberCoroutineScope()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = true,
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = height,
            onValueChange = { height = it }, // we don't need to convert it to double by hand anymore
            label = { Text("Height (cm)") },
            modifier = Modifier.fillMaxWidth()
                .testTag("HeightTextField")
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = true,
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Weight (kg)") },
            modifier = Modifier.fillMaxWidth()
                .testTag("WeightTextField")
        )
        Spacer(modifier = Modifier.height(16.dp))
        val keyboardController = LocalSoftwareKeyboardController.current
        Button(
            onClick = {
                keyboardController?.hide()
                val heightVal = height.toFloatOrNull()
                val weightVal = weight.toFloatOrNull()
                if (heightVal != null && weightVal != null && heightVal > 0 && weightVal > 0) {
                    calculateBMI(heightVal, weightVal).onSuccess { bmi ->
                        val (text, color) = getBMIResultTextAndColor(bmi)
                        resultText = text
                        resultColor = color
                    }.onFailure {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Please enter valid height and weight",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                } else {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Height and weight must be greater than 0",
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
                .semantics { contentDescription = "Calculate"} // for testing
        ) {
            Text("Calculate")
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = resultText,
            color = resultColor,
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize
        )
        // Snackbar
        SnackbarHost(hostState = snackbarHostState)
    }
}

fun calculateBMI(height: Float, weight: Float): Result<Float> {
    if (height == 0f || weight == 0f) {
        return Result.failure(Exception("Please enter valid height and weight"))
    }
    val heightInMeters = height / 100
    val bmi = weight / (heightInMeters * heightInMeters)
    return Result.success(bmi)
}






fun getBMIResultTextAndColor(bmi: Float): Pair<String, Color> {
    return when {
        bmi < 18.5 -> "Underweight" to Color.Blue
        bmi < 24.9 -> "Normal" to Color.Green
        bmi < 29.9 -> "Overweight" to Color(0xFFFFA500) // Orange color
        bmi < 40 -> "Obese" to Color.Red
        else -> "Class 3 Obesity" to Color(0xFF8B0000) // Dark red color
    }.let { (category, color) ->
        String.format("BMI: %.2f\n$category", bmi) to color
    }
}

@Preview(showBackground = true)
@Composable
fun BMICalculatorPreview() {
    BMIKotlinTheme {
        BMICalculator()
    }
}
