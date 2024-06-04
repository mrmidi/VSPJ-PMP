package net.mrmidi.pmp.bank

//import androidx.lifecycle.viewmodel.compose.viewModel

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import net.mrmidi.pmp.bank.ui.screens.ConfirmationScreen
import net.mrmidi.pmp.bank.ui.screens.HomeScreen
import net.mrmidi.pmp.bank.ui.screens.LoginScreen
import net.mrmidi.pmp.bank.ui.screens.PaymentScreen
import net.mrmidi.pmp.bank.viewmodel.HomeViewModel
import net.mrmidi.pmp.bank.viewmodel.LoginViewModel


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppContent()
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppContent() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            // Assume LoginScreen handles navigation upon successful login
            val loginViewModel: LoginViewModel = viewModel()
            LoginScreen(loginViewModel = loginViewModel, navController = navController)
        }
        composable(
            route = "home/{accountId}",
            arguments = listOf(navArgument("accountId") { type = NavType.IntType })
        ) { backStackEntry ->
            // Extract the accountId argument
            val accountId = backStackEntry.arguments?.getInt("accountId")
            if (accountId != null) {
                val homeViewModel: HomeViewModel = viewModel()
                HomeScreen(homeViewModel = homeViewModel, accountId = accountId, navController = navController)
            }
        }
        composable(
            route = "payment/{accountId}",
            arguments = listOf(navArgument("accountId") { type = NavType.IntType })
        ) { backStackEntry ->
            val accountId = backStackEntry.arguments?.getInt("accountId") ?: throw IllegalStateException("Account ID not found")
            PaymentScreen(navController = navController, accountId = accountId)
        }
        composable(
            route = "confirmation/{transactionId}/{isSuccess}/{message}/{accountId}",
            arguments = listOf(
                navArgument("transactionId") { type = NavType.StringType },
                navArgument("isSuccess") { type = NavType.BoolType },
                navArgument("message") { type = NavType.StringType; nullable = true } // Assuming message can be null
            )
        ) { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getString("transactionId") ?: "Unknown"
            val isSuccess = backStackEntry.arguments?.getBoolean("isSuccess") ?: false
            val message = backStackEntry.arguments?.getString("message") ?: "Unknown error occurred."

            ConfirmationScreen(transactionId = transactionId, navController = navController, isSuccess = isSuccess, message = message)
        }

    }
}

/* w/o navcontroller

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppContent() {
    val navController = rememberNavController() // Create a NavController

    val loginViewModel: LoginViewModel = viewModel()
    // Use observeAsState for LiveData. Provide a default value for the initial state.
    val accountId by loginViewModel.accountId

    Surface(color = MaterialTheme.colorScheme.background) {
        if (accountId != null) {
            println("Account ID is set: $accountId")
            // If accountId is set, navigate to the HomeScreen
            val homeViewModel: HomeViewModel = viewModel()
            HomeScreen(homeViewModel, accountId!!)
        } else {
            // If accountId is not set, show LoginScreen
            println("Account ID is not set, showing LoginScreen")
            LoginScreen(loginViewModel)
        }
    }
}

*/