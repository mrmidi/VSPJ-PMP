package net.mrmidi.pmp.bank.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.mrmidi.pmp.bank.network.Operation
import net.mrmidi.pmp.bank.network.RetrofitInstance


class HomeViewModel : ViewModel() {
    private val _paymentHistory = MutableStateFlow<List<Operation>>(emptyList())
    val paymentHistory = _paymentHistory.asStateFlow()

    private val _currentBalance = MutableStateFlow("0.00")
    val currentBalance = _currentBalance.asStateFlow()

    fun fetchPaymentHistory(accountId: Int, beforeDate: String? = null, type: String? = null) {
        viewModelScope.launch {
            val response = RetrofitInstance.api.getPaymentHistory(accountId, beforeDate, type)
            println("Created request for accountId: $accountId")
            println("Response: $response")
            if (response.isSuccessful && response.body() != null) {
                println("Operations list recieved: ${response.body()!!.operations}")
                println("Current balance: ${response.body()!!.current_balance}")
                _paymentHistory.value = response.body()!!.operations
                _currentBalance.value = response.body()!!.current_balance
            } else {
                // will handle errors here
            }
        }
    }

}


