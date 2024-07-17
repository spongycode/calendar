package com.spongycode.calendar.screen.calendar.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog

@Composable
fun MonthYearPickerDialog(
    year: Int,
    month: Int,
    onDismissRequest: () -> Unit,
    onMonthYearSelected: (Int, Int) -> Unit
) {
    Dialog(content = {
        MonthYearPicker(
            currentYear = year,
            currentMonth = month,
            onDismissRequest = { onDismissRequest() },
            onMonthYearSelected = { month, year -> onMonthYearSelected(month, year) }
        )
    }, onDismissRequest = { onDismissRequest() })
}