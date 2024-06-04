package net.mrmdi.pmp.bmi_kotlin.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import net.mrmdi.pmp.bmi_kotlin.viewmodel.BMIViewModel

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun InputScreen(navController: NavHostController, viewModel: BMIViewModel) {

    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("VŠPJ/PMP BMI Calculator") },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false,
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
                value = height,
                onValueChange = { height = it },
                label = { Text("Height (cm)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false,
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                ),
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Weight (kg)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            val keyboardController = LocalSoftwareKeyboardController.current
            Button(
                onClick = {
                    keyboardController?.hide()
                    val heightValue = height.toDoubleOrNull() ?: 0.0
                    val weightValue = weight.toDoubleOrNull() ?: 0.0

                    if (heightValue > 0 && weightValue > 0) {
                        viewModel.calculateBMI(height, weight)
                        navController.navigate("resultScreen")
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Height and Weight must be greater than 0",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                // vector image
                Icon(
                    imageVector = ImageVector.vectorResource(id = net.mrmdi.pmp.bmi_kotlin.R.drawable.calc_icon),
                    contentDescription = "Calculate BMI",
                    modifier = Modifier.size(24.dp)
                )
                Text("Calculate")
            }
        }
    }
}



// add preview
@Preview
@Composable
fun InputScreenPreview() {
    // Create a dummy NavHostController for preview purposes
    val navController = rememberNavController()
    InputScreen(navController = navController, viewModel = BMIViewModel())
}
