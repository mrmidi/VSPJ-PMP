package net.mrmidi.pmp.bank.viewmodel


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.mrmidi.pmp.bank.network.LoginRequest
import net.mrmidi.pmp.bank.network.RetrofitInstance

class LoginViewModel : ViewModel() {
    private var _loginSuccess = mutableStateOf<Boolean?>(null)
    private var _accountId = mutableStateOf<Int?>(null)

    // Navigation event state flow
    private val _navigationEvent = MutableStateFlow<LoginNavigationEvent?>(null)
    val navigationEvent = _navigationEvent.asStateFlow()

    val loginSuccess: MutableState<Boolean?> = _loginSuccess
    val accountId: MutableState<Int?> = _accountId

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val response = RetrofitInstance.api.loginUser(LoginRequest(username, password))
            if (response.isSuccessful && response.body() != null) {
                _loginSuccess.value = true
                _accountId.value = response.body()!!.accountId
                // Emit navigation event
                println("Login is successful from login method of LoginViewModel: ${response.body()?.message}")
                _navigationEvent.value = LoginNavigationEvent.NAVIGATE_TO_HOME_SCREEN

            } else {
                _loginSuccess.value = false
            }
        }
    }

    // Function to reset navigation event to avoid multiple navigations
    fun onNavigationEventHandled() {
        _navigationEvent.value = null
    }
}



enum class LoginNavigationEvent {
    NAVIGATE_TO_HOME_SCREEN
}
