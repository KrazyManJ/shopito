package dev.krazymanj.shopito.ui.elements.chip

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.composables.icons.lucide.Calendar
import com.composables.icons.lucide.Lucide
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.ui.elements.modal.CustomDatePickerDialog
import dev.krazymanj.shopito.utils.DateUtils

@Composable
fun DatePickerChip(
    date: Long?,
    onDateChange: (Long?) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    PickerChip(
        selected = date != null,
        onClick = { showDatePicker = true },
        leadingIcon = Lucide.Calendar,
        label = when {
            date != null -> DateUtils.getDateString(date)
            else -> stringResource(R.string.date_label)
        },
        onXClick = {
            onDateChange(null)
        },
    )

    if (showDatePicker) {
        CustomDatePickerDialog(
            date = date,
            onDateSelected = { onDateChange(it) },
            onDismiss = { showDatePicker = false }
        )
    }
}