package com.example.routeexplorer2.utils

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    setDateRange: (String) -> Unit,
    onDismiss: () -> Unit,
) {

    // Date Range Picker State
    val dateRangePickerState = rememberDateRangePickerState()


    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            DateRangePicker(
                state = dateRangePickerState,
                title = {
                    Text(text = "Select date range to assign the chart", modifier = Modifier
                        .padding(16.dp))
                },
                headline = {
                    Row(modifier = Modifier.fillMaxWidth()
                        .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(Modifier.weight(1f)) {
                            (if(dateRangePickerState.selectedStartDateMillis!=null) dateRangePickerState.selectedStartDateMillis?.let { getFormattedDate(it) } else "Start Date")?.let { Text(text = it, style = MaterialTheme.typography.labelSmall ) }
                        }
                        Box(Modifier.weight(1f)) {
                            (if(dateRangePickerState.selectedEndDateMillis!=null) dateRangePickerState.selectedEndDateMillis?.let { getFormattedDate(it) } else "End Date")?.let { Text(text = it, style = MaterialTheme.typography.labelSmall) }
                        }
                    }
                },
                showModeToggle = true,
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    val startMillis = dateRangePickerState.selectedStartDateMillis
                    val endMillis = dateRangePickerState.selectedEndDateMillis
                    if (startMillis != null && endMillis != null) {
                        val formattedDateRange = getDateString(startMillis, endMillis)
                        setDateRange(formattedDateRange)
                        onDismiss()
                    }

                }
            ) {
                Text("Apply")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

private fun getDateString(start: Long, end: Long): String {
    Log.i("ApplyFilters", "StartLong: $start, EndLong: $end")
    // Format a date range as a string
    val startStr = DateFormat.getDateInstance().format(Date(start))
    val endStr = DateFormat.getDateInstance().format(Date(end))
    Log.i("ApplyFilters", "StartStr: $startStr, EndStr: $endStr")
    return "$startStr - $endStr"
}

fun getFormattedDate(timeInMillis: Long): String {
    val calender = Calendar.getInstance()
    calender.timeInMillis = timeInMillis
    val dateFormat = SimpleDateFormat("dd/MM/yyyy")
    return dateFormat.format(calender.timeInMillis)
}