package net.mrmidi.pmp.bank.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.mrmidi.pmp.bank.network.PaymentRequest
import net.mrmidi.pmp.bank.network.RetrofitInstance


class PaymentViewModel : ViewModel() {
    private var _paymentStatus = mutableStateOf<Pair<Boolean?, String?>>(null to null)
    val paymentStatus: MutableState<Pair<Boolean?, String?>> = _paymentStatus

    private var _paymentId = mutableStateOf<String?>(null)
    val paymentId: MutableState<String?> = _paymentId

    private val _paymentSuccessEvent = MutableStateFlow<String?>(null)
    val paymentSuccessEvent = _paymentSuccessEvent.asStateFlow()

    fun makePayment(paymentInfo: PaymentRequest) {
        viewModelScope.launch {
            try {
                println("Creating payment request: $paymentInfo")
                val response = RetrofitInstance.api.makePayment(paymentInfo)
                println("Payment response: $response")
                if (response.isSuccessful && response.body() != null) {
                    print("Payment successful: ${response.body()!!.payment_id}")
                    _paymentStatus.value = true to response.body()!!.message
                    _paymentId.value = response.body()!!.payment_id.toString()
                    // Trigger navigation event
                    _paymentSuccessEvent.value = _paymentId.value
                } else {
                    println("Payment failed: ${response.errorBody()?.string()}")
                    _paymentStatus.value = false to "Payment failed: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                println("An error occurred: ${e.localizedMessage}")
                _paymentStatus.value = false to "An error occurred: ${e.localizedMessage}"
            }
        }
    }

    // Function to reset navigation event to avoid multiple navigations
    fun onPaymentSuccessNavigationComplete() {
        _paymentSuccessEvent.value = null
    }
}


//class PaymentViewModel(private val apiService: ApiService) : ViewModel() {
//
//    private var _paymentStatus = mutableStateOf<Pair<Boolean?, String?>>(null to null)
//    val paymentStatus: MutableState<Pair<Boolean?, String?>> = _paymentStatus
//
//    var paymentId = mutableStateOf<String?>(null)
//    var paymentSuccessEvent = mutableStateOf<String?>(null)
//
//    fun makePayment(paymentInfo: PaymentRequest) {
//        viewModelScope.launch {
//            try {
//                val response = apiService.makePayment(paymentInfo)
//                if (response.isSuccessful && response.body() != null) {
//                    paymentStatus.value = true to response.body()!!.message
//                    paymentId.value = response.body()!!.payment_id.toString()
//                    // Trigger the navigation event
//                    paymentSuccessEvent.value = paymentId.value
//                } else {
//                    paymentStatus.value = false to "Payment failed: ${response.errorBody()?.string()}"
//                }
//            } catch (e: Exception) {
//                paymentStatus.value = false to "An error occurred: ${e.localizedMessage}"
//            }
//        }
//    }
//
//    // Reset the navigation trigger to prevent repeated navigation
//    fun onPaymentSuccessNavigationComplete() {
//        paymentSuccessEvent.value = null
//    }
//}
