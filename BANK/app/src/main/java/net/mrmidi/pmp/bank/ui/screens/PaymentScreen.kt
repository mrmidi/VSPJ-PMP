package net.mrmidi.pmp.bank.ui.screens

// import androidx.compose.material.*

// showDatePickerDialog

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import net.mrmidi.pmp.bank.network.PaymentRequest
import net.mrmidi.pmp.bank.network.Transaction
import net.mrmidi.pmp.bank.ui.components.showDatePickerDialog
import net.mrmidi.pmp.bank.viewmodel.PaymentViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O) // Required for LocalDate
@Composable
fun PaymentScreen(navController: NavHostController, accountId: Int) {

    val paymentViewModel: PaymentViewModel = viewModel()
    val paymentStatus = paymentViewModel.paymentStatus.value
    var transaction by remember { mutableStateOf(Transaction()) }
    val datePickerDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    // dateFormatter
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    // Use collectAsState() to observe changes to paymentSuccessEvent
    val paymentSuccessEvent = paymentViewModel.paymentSuccessEvent.collectAsState().value



    // Observe paymentSuccessEvent for navigation
    LaunchedEffect(paymentSuccessEvent) {
        paymentSuccessEvent?.let { paymentId ->
            // Extract isSuccess and message from somewhere or define them
            val isSuccess = paymentStatus.first ?: false
            val message = paymentStatus.second ?: "Success" // Default message or extract from somewhere

            // Construct navigation route string
            val route = buildString {
                append("confirmation/$paymentId/$isSuccess/")
                append(URLEncoder.encode(message, StandardCharsets.UTF_8.toString()))
                append("/$accountId")
            }

            navController.navigate(route) {
                // Here you can specify NavOptions or popBackStack behavior if needed
            }

            // Reset the event to avoid re-navigation on recomposition
            paymentViewModel.onPaymentSuccessNavigationComplete()
        }
    }




    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Make a Payment", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = transaction.recipientAccount,
            onValueChange = { transaction = transaction.copy(recipientAccount = it) },
            label = { Text("Recipient Account") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = transaction.amount,
            onValueChange = { transaction = transaction.copy(amount = it) },
            label = { Text("Amount") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = transaction.bankCode.orEmpty(),
            onValueChange = { transaction = transaction.copy(bankCode = it) },
            label = { Text("Bank Code (optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        // variable symbol, specific symbol, constant symbol, message for recipient, message for sender
        OutlinedTextField(
            value = transaction.variableSymbol.orEmpty(),
            onValueChange = { transaction = transaction.copy(variableSymbol = it) },
            label = { Text("Variable Symbol (optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = transaction.specificSymbol.orEmpty(),
            onValueChange = { transaction = transaction.copy(specificSymbol = it) },
            label = { Text("Specific Symbol (optional)") },
            modifier = Modifier.fillMaxWidth()
        )
        
        OutlinedTextField(
            value = transaction.constantSymbol.orEmpty(),
            onValueChange = { transaction = transaction.copy(constantSymbol = it) },
            label = { Text("Constant Symbol (optional)") },
            modifier = Modifier.fillMaxWidth()
        )



        val datePickerDialog = remember { mutableStateOf(false) }
        OutlinedTextField(
            value = transaction.dueDate.orEmpty(),
            onValueChange = { /* Read-Only Field */ },
            label = { Text("Due Date (optional)") },
            trailingIcon = {
                IconButton(onClick = { datePickerDialog.value = true }) {
                    Icon(Icons.Filled.DateRange, contentDescription = "Select Date")
                }
            },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )

        if (datePickerDialog.value) {
            LaunchedEffect(Unit) {
                showDatePickerDialog(context) { selectedDate ->
                    transaction = transaction.copy(dueDate = selectedDate.format(dateFormatter))
                    datePickerDialog.value = false
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val paymentId by paymentViewModel.paymentId // Get the payment ID from the ViewModel



        Button(
            onClick = {
                // Handle the payment submission
                val paymentRequest = PaymentRequest(
                    account_id = accountId,
                    amount = transaction.amount.toDouble(),
                    recipient_account = transaction.recipientAccount,
                    bank_code = transaction.bankCode,
                    variable_symbol = transaction.variableSymbol,
                    specific_symbol = transaction.specificSymbol,
                    constant_symbol = transaction.constantSymbol,
                    message_for_recipient = transaction.messageForRecipient,
                    message_for_sender = transaction.messageForSender,
                    due_date = transaction.dueDate
                )
                paymentViewModel.makePayment(paymentRequest)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Submit Payment")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewPaymentScreen() {
    // dummy nav controller
    var navController = rememberNavController()
    val accountId = 1
    PaymentScreen(navController, accountId)
}

/*
ENDPOINT CODE AS REFERENCE
function makePayment() {

    /* DB SCHEMA
    CREATE TABLE operations (
    operation_id INT AUTO_INCREMENT PRIMARY KEY,
    account_id INT NOT NULL,
    operation_type ENUM('deposit', 'withdrawal', 'payment') NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    operation_date DATE NOT NULL,
    recipient_account VARCHAR(20), -- Optional, for payments
    bank_code VARCHAR(10), -- Optional, for payments to other banks
    variable_symbol VARCHAR(20), -- Optional, could be used for payment identification
    specific_symbol VARCHAR(20), -- Optional, another identifier for payments
    constant_symbol VARCHAR(20), -- Optional, another payment identifier
    message_for_recipient TEXT, -- Optional, message to the payment recipient
    message_for_sender TEXT, -- Optional, confirmation message or note to self
    due_date DATE, -- Optional, mainly for scheduled payments
    status ENUM('pending', 'completed', 'failed') DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES accounts(account_id)
);
    */

    global $conn; // Use the DBAL connection

    $input = json_decode(file_get_contents('php://input'), true);
    $accountId = $input['account_id'] ?? null;
    $amount = $input['amount'] ?? null;
    $recipientAccount = $input['recipient_account'] ?? null;
    $bankCode = $input['bank_code'] ?? null;
    $operationDate = date('Y-m-d'); // set the operation date to today
    $variableSymbol = $input['variable_symbol'] ?? '';
    $specificSymbol = $input['specific_symbol'] ?? '';
    $constantSymbol = $input['constant_symbol'] ?? '';
    $messageForRecipient = $input['message_for_recipient'] ?? '';
    $messageForSender = $input['message_for_sender'] ?? '';
    $dueDate = $input['due_date'] ?? null;

    $status = 'pending'; // or 'completed' based on your logic
    // check if operation date is today, if it is, set status to completed (quick payment)
    if ($dueDate === date('Y-m-d')) {
        $status = 'completed';
    }

    if (empty($accountId) || empty($amount)) {
        http_response_code(400); // Bad Request
        echo json_encode(['error' => 'Account ID and amount are required']);
        return;
    }

    try {
        // Start a database transaction
        $conn->beginTransaction();

        // Verify account exists and has sufficient funds
        $queryBuilder = $conn->createQueryBuilder();
        $account = $queryBuilder
            ->select('balance')
            ->from('accounts')
            ->where('account_id = :accountId')
            ->setParameter('accountId', $accountId)
            ->executeQuery()
            ->fetchAssociative();

        if (!$account) {
            http_response_code(400); // Bad Request
            echo json_encode(['error' => 'Account not found']);
            return;
        }

        if ($account['balance'] < $amount) {
            http_response_code(400); // Bad Request
            echo json_encode(['error' => 'Insufficient funds']);
            return;
        }

        // Insert the new payment into the operations table
        $queryBuilder = $conn->createQueryBuilder();
        $queryBuilder
            ->insert('operations')
            ->values([
                'account_id' => ':accountId',
                'operation_type' => ':operationType',
                'amount' => ':amount',
                'operation_date' => ':operationDate',
                'recipient_account' => ':recipientAccount',
                'bank_code' => ':bankCode',
                'variable_symbol' => ':variableSymbol',
                'specific_symbol' => ':specificSymbol',
                'constant_symbol' => ':constantSymbol',
                'message_for_recipient' => ':messageForRecipient',
                'message_for_sender' => ':messageForSender',
                'due_date' => ':dueDate',
                'status' => ':status'
            ])
            ->setParameters([
                'accountId' => $accountId,
                'operationType' => 'withdrawal',
                'amount' => $amount,
                'operationDate' => $operationDate,
                'recipientAccount' => $recipientAccount,
                'bankCode' => $bankCode,
                'variableSymbol' => $variableSymbol,
                'specificSymbol' => $specificSymbol,
                'constantSymbol' => $constantSymbol,
                'messageForRecipient' => $messageForRecipient,
                'messageForSender' => $messageForSender,
                'dueDate' => $dueDate,
                'status' => $status
            ])
            ->executeStatement();

        // Update the account balance
        $queryBuilder = $conn->createQueryBuilder();
        $queryBuilder
            ->update('accounts')
            ->set('balance', 'balance - :amount') // Subtract the amount from the balance
            ->where('account_id = :accountId')
            ->setParameters([
                'amount' => $amount,
                'accountId' => $accountId
            ])
            ->executeStatement();



        // Commit the transaction
        $conn->commit();

        http_response_code(200); // OK
        echo json_encode(['message' => 'Payment processed successfully']);
    } catch (\Doctrine\DBAL\Exception $e) {
        // Rollback the transaction in case of an error
        $conn->rollBack();

        http_response_code(500); // Internal Server Error
        echo json_encode(['error' => 'An error occurred while processing the payment: ' . $e->getMessage()]);
    }
}


 */