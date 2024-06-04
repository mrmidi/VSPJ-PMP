package net.mrmidi.pmp.bank.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.mrmidi.pmp.bank.network.Operation

@Composable
fun PaymentListItem(
    operation: Operation,
    accountId: Int,
    onItemClick: (accountId: Int, operationId: Int) -> Unit
) {
    // Define light red and light green
    val lightRed = Color(0xFFEFB8C8)
    val lightGreen = Color(0xFFC8EFB8)

    val (emoji, color) = when (operation.operation_type) {
        "withdrawal" -> Pair("⬇️", lightRed)
        "deposit" -> Pair("⬆️", lightGreen)
        else -> Pair("", Color.Black)
    }

    // Reformat date to DD.MM.YYYY from YYYY-MM-DD
    val dateParts = operation.operation_date.split("-")
    val formattedDate = "${dateParts[2]}.${dateParts[1]}.${dateParts[0]}"

    Surface(
        color = color,
        shape = RoundedCornerShape(8.dp), // Round the corners
        border = BorderStroke(1.dp, Color.Gray), // Add border stroke
        modifier = Modifier.padding(4.dp) // Add padding around the Surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onItemClick(accountId, operation.operation_id) }
                .padding(16.dp) // Add padding inside the Row for a larger clickable area
        ) {
            Text(text = "$formattedDate: $emoji ${operation.amount}")
        }
    }
}
