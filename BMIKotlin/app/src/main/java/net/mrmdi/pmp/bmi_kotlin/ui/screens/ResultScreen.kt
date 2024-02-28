package net.mrmdi.pmp.bmi_kotlin.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import net.mrmdi.pmp.bmi_kotlin.ui.BMIViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(navController: NavHostController, viewModel: BMIViewModel) {
    // Listen for changes in BMI calculation result

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your BMI Result") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(2.dp, Color.Black)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Large Text for Result
                    Text(
                        text = viewModel.bmiFormatted,
                        style = MaterialTheme.typography.displayLarge, // Large font for result
                        color = viewModel.bmiColor
                    )
                    // Description Text
                    Text(
                        text = viewModel.bmiDescription, // Placeholder, use actual category from ViewModel
                        style = MaterialTheme.typography.bodyLarge, // Medium-sized font for description
                        color = viewModel.bmiColor
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ResultScreenPreview() {
    // Create a dummy NavHostController for preview purposes
    val navController = rememberNavController()
    ResultScreen(navController = navController, viewModel = BMIViewModel()) // Make sure you have a preview instance or mock
}
