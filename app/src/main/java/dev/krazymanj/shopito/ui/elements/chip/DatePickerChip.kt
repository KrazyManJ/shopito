package dev.krazymanj.shopito.ui.elements.chip

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.composables.icons.lucide.Calendar
import com.composables.icons.lucide.Lucide
import dev.krazymanj.shopito.ui.elements.dialog.CustomDatePickerDialog
import dev.krazymanj.shopito.utils.DateUtils

@Composable
fun DatePickerChip(
    date: Long?,
    onDateChange: (Long) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    PickerChip(
        selected = date != null,
        onClick = { showDatePicker = true },
        leadingIcon = Lucide.Calendar,
        label = when {
            date != null -> DateUtils.getDateString(date)
            else -> "Date"
        }
    )

    if (showDatePicker) {
        CustomDatePickerDialog(
            date = date,
            onDateSelected = { onDateChange(it) },
            onDismiss = { showDatePicker = false }
        )
    }
}