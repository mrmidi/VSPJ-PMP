package net.mrmidi.pmp.bank.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import net.mrmidi.pmp.bank.ui.components.PaymentListItem
import net.mrmidi.pmp.bank.ui.components.showDatePickerDialog
import net.mrmidi.pmp.bank.viewmodel.HomeViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O) // Required for LocalDate
@Composable
fun HomeScreen(homeViewModel: HomeViewModel = viewModel(), accountId: Int, navController: NavController) {
    val context = LocalContext.current
    val paymentHistory by homeViewModel.paymentHistory.collectAsState(initial = emptyList())
    val currentBalance by homeViewModel.currentBalance.collectAsState(initial = "0.00")
    val selectedDate = remember { mutableStateOf<LocalDate?>(null) }
    val dateFormatter = remember { DateTimeFormatter.ofPattern("yyyy-MM-dd") }
    val (selectedType, setSelectedType) = remember { mutableStateOf("All") }


    LaunchedEffect(Unit) {
        println("Fetching payment history for account: $accountId")
        homeViewModel.fetchPaymentHistory(accountId)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Balance and Buttons
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { // add padding to the elements

            Button(onClick = { /* Navigate to New Payment */
                println("Navigating to New Payment")
                // Open the New Payment screen
                navController.navigate("payment/$accountId")


            }) {
                Text("New Payment")
            }
            Button(onClick = { /* Navigate to QR Payment */ }) {
                Text("QR Payment")
            }
        }

        Spacer(modifier = Modifier.height(16.dp)) // Add a spacer for better separation

        Text(
            text = "Balance: $currentBalance",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp)) // Add a spacer for better separation

        // Date and Type Filter
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            // Date Picker Button
            Button(onClick = {
                showDatePickerDialog(context) { date ->
                    selectedDate.value = date
                }
            }) {
                Text(text = selectedDate.value?.format(dateFormatter) ?: "Select Date")
            }

            // Payment Type Toggle
            Row {
                ToggleButton(
                    text = "All",
                    isSelected = selectedType == "All",
                    onClick = { setSelectedType("All") },
                    modifier = Modifier.padding(end = 3.dp) // Add padding to the end (right) of the button
                )
                ToggleButton(
                    text = "Withdrawals",
                    isSelected = selectedType == "Withdrawal",
                    onClick = { setSelectedType("Withdrawal") },
                    modifier = Modifier.padding(horizontal = 3.dp) // Add padding horizontally around the button
                )
                ToggleButton(
                    text = "Deposits",
                    isSelected = selectedType == "Deposit",
                    onClick = { setSelectedType("Deposit") },
                    modifier = Modifier.padding(start = 3.dp) // Add padding to the start (left) of the button
                )
            }


            Row {
                // Apply Filters Button
                Button(onClick = {
                    // Implement filter application logic here
                    // For example: homeViewModel.fetchFilteredData(accountId, selectedDate, selectedType)
                    println("Applying filters for account: $accountId)")
                    println("Selected date: ${selectedDate.value?.format(dateFormatter)}")
                    println("Selected type: $selectedType")
                    homeViewModel.fetchPaymentHistory(accountId, selectedDate.value?.format(dateFormatter), selectedType.lowercase())
                }) {
                    Text("Apply Filters")
                }
                // cancel Filters Button
                Button(onClick = {
                    // Implement filter application logic here
                    // For example: homeViewModel.fetchFilteredData(accountId, selectedDate, selectedType)
                    println("Cancel filters for account: $accountId)")
                    println("Selected date: ${selectedDate.value?.format(dateFormatter)}")
                    println("Selected type: $selectedType")
                    homeViewModel.fetchPaymentHistory(accountId)
                }) {
                    Text("Cancel Filters")
                }
            }



            // Payment List
            LazyColumn(modifier = Modifier.padding(vertical = 8.dp).fillMaxSize()) {
                items(items = paymentHistory, itemContent = { operation ->
                    PaymentListItem(
                        operation = operation,
                        accountId = accountId,
                        onItemClick = { _, _ ->
                            // Handle item click
                            println("Clicked on operation: ${operation.operation_id} for account: $accountId")
                        })
                })
            }
        }
    }
}


    @Composable
    fun ToggleButton(text: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier) {
        Button(
            modifier = modifier,
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(text = text)
        }
    }
//
//@RequiresApi(Build.VERSION_CODES.O) // Required for LocalDate
////@Composable
//fun showDatePickerDialog(context: Context, onDateSelected: (LocalDate) -> Unit) {
//    val current = LocalDate.now()
//    android.app.DatePickerDialog(
//        context,
//        { _, year, month, dayOfMonth ->
//            val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
//            onDateSelected(selectedDate)
//        },
//        current.year,
//        current.monthValue - 1,
//        current.dayOfMonth
//    ).show()
//}
//
