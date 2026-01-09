package dev.krazymanj.shopito.ui.elements.modal

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.ui.theme.backgroundSecondaryColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePickerDialog(
    modifier: Modifier = Modifier,
    date: Long? = null,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = date)

    DatePickerDialog(
        modifier = modifier,
        onDismissRequest = { onDismiss() },
        colors = DatePickerDefaults.colors(
            containerColor = backgroundSecondaryColor(),

        ),
        confirmButton = {
            TextButton(
                enabled = datePickerState.selectedDateMillis != null,
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        onDateSelected(millis)
                        onDismiss()
                    }
                }
            ) {
                Text(text = stringResource(R.string.select_label))
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismiss()
            }) {
                Text(text = stringResource(R.string.cancel_label))
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                containerColor = Color.Transparent
            )
        )
    }
}
