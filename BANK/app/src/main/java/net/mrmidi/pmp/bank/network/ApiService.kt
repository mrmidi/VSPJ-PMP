package net.mrmidi.pmp.bank.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("/vspj/pmp/api/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("/vspj/pmp/api/payment-history/{accountId}")
    suspend fun getPaymentHistory(
        @Path("accountId") accountId: Int,
        @Query("before_date") beforeDate: String? = null,
        @Query("type") type: String? = null
    ): Response<PaymentHistoryResponse>

    @POST("/vspj/pmp/api/make-payment")
    suspend fun makePayment(@Body paymentRequest: PaymentRequest): Response<PaymentResponse>
}

data class LoginRequest(val username: String, val password: String)

data class LoginResponse(val message: String, val accountId: Int)

data class PaymentHistoryResponse(
    val current_balance: String,
    val operations: List<Operation>
)

data class Operation(
    val operation_id: Int,
    val operation_type: String,
    val amount: String,
    val operation_date: String
    // will add other fields as needed
)

data class Transaction(
    var recipientAccount: String = "",
    var bankCode: String? = null,
    var amount: String = "",
    var variableSymbol: String? = null,
    var specificSymbol: String? = null,
    var constantSymbol: String? = null,
    var messageForRecipient: String? = null,
    var messageForSender: String? = null,
    var dueDate: String? = null
)

data class PaymentRequest(
    val account_id: Int,
    val amount: Double, // Use a numeric type for amount
    val recipient_account: String?,
    val bank_code: String?,
    val variable_symbol: String?,
    val specific_symbol: String?,
    val constant_symbol: String?,
    val message_for_recipient: String?,
    val message_for_sender: String?,
    val due_date: String?
)

data class PaymentResponse(
    val message: String,
    val payment_id: Int
    // will other response fields if needed
)