package net.mrmdi.pmp.bmi_kotlin.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import net.mrmdi.pmp.bmi_kotlin.ui.BMIViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputScreen(navController: NavHostController, viewModel: BMIViewModel) {
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

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
                viewModel.calculateBMI(height, weight)
                navController.navigate("resultScreen")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = net.mrmdi.pmp.bmi_kotlin.R.drawable.calc_icon),
                contentDescription = "Calculate BMI", //
                modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(8.dp))
            Text("Calculate")
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
