package net.mrmidi.pmp.bank.ui.components

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O) // Required for LocalDate
//@Composable
fun showDatePickerDialog(context: Context, onDateSelected: (LocalDate) -> Unit) {
    val current = LocalDate.now()
    android.app.DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
            onDateSelected(selectedDate)
        },
        current.year,
        current.monthValue - 1,
        current.dayOfMonth
    ).show()
}