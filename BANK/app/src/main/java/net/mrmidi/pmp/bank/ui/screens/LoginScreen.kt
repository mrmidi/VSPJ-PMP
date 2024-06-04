package net.mrmidi.pmp.bank.ui.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import net.mrmidi.pmp.bank.viewmodel.LoginNavigationEvent
import net.mrmidi.pmp.bank.viewmodel.LoginViewModel

@Composable
fun LoginScreen(loginViewModel: LoginViewModel, navController: NavController) {
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val loginSuccess by loginViewModel.loginSuccess
    val navigationEvent by loginViewModel.navigationEvent.collectAsState()

    // Check for navigation event
    LaunchedEffect(navigationEvent) {
        when (navigationEvent) {
            LoginNavigationEvent.NAVIGATE_TO_HOME_SCREEN -> {
                // Perform navigation
                navController.navigate("home/${loginViewModel.accountId.value}")
                loginViewModel.onNavigationEventHandled()
            }
            else -> Unit
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // Header
        Text("VSPJ BANK", style = MaterialTheme.typography.headlineLarge)
        TextField(
            value = username.value,
            onValueChange = { username.value = it },
            label = { Text("Username") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { loginViewModel.login(username.value, password.value)
        }) {
            Text("Login")
        }
    }
}


//// prievew
//@Preview(showBackground = true)
//@Composable
//fun LoginScreenPreview() {
//    BANKTheme {
//        LoginScreen(LoginViewModel())
//    }
//}