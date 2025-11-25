package dev.krazymanj.shopito.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.ArrowUp
import com.composables.icons.lucide.Calendar
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MapPin
import dev.krazymanj.shopito.ui.theme.Primary
import dev.krazymanj.shopito.ui.theme.backgroundPrimaryColor
import dev.krazymanj.shopito.ui.theme.spacing16
import dev.krazymanj.shopito.ui.theme.spacing8
import dev.krazymanj.shopito.ui.theme.textPrimaryColor
import dev.krazymanj.shopito.ui.theme.textSecondaryColor
import dev.krazymanj.shopito.utils.DateUtils



@Composable
fun QuickAdd(
    value: String,
    date: Long?,
    latitude: Double?,
    longitude: Double?,

    onValueChange: (String) -> Unit,
    onDateChange: (Long) -> Unit,
    onLocationChangeRequest: () -> Unit,

    onAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }

    val PIN_COLORS = SelectableChipColors(
        containerColor = backgroundPrimaryColor(),
        labelColor = textPrimaryColor(),
        leadingIconColor = textPrimaryColor(),
        trailingIconColor = textPrimaryColor(),
        disabledContainerColor = backgroundPrimaryColor(),
        disabledLabelColor = textSecondaryColor(),
        disabledLeadingIconColor = textSecondaryColor(),
        disabledTrailingIconColor = textSecondaryColor(),
        selectedContainerColor = Primary,
        disabledSelectedContainerColor = textSecondaryColor(),
        selectedLabelColor = backgroundPrimaryColor(),
        selectedLeadingIconColor = backgroundPrimaryColor(),
        selectedTrailingIconColor = backgroundPrimaryColor()
    )

    Column(
        modifier = modifier.then(Modifier
            .fillMaxWidth()
            .background(backgroundPrimaryColor())
            .padding(horizontal = spacing16, vertical = spacing8)
            .navigationBarsPadding())
    ) {
        HorizontalDivider(
            modifier = Modifier.padding(vertical = spacing8),
            color = textSecondaryColor()
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InputChip(
                date != null,
                onClick = { showDatePicker = true },
                label = {
                    Text(text = when {
                        date != null -> DateUtils.getDateString(date)
                        else -> "Date"
                    })
                },
                leadingIcon = {
                    Icon(
                        imageVector = Lucide.Calendar,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                },
                colors = PIN_COLORS
            )
            InputChip(
                selected = latitude != null && longitude != null,
                onClick = onLocationChangeRequest,
                leadingIcon = {
                    Icon(
                        imageVector = Lucide.MapPin,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                },
                label = {
                    Text(text = when {
                        latitude != null && longitude != null -> "Selected"
                        else -> "Location"
                    })
                },
                colors = PIN_COLORS
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = "Add an Item (2x for two items)...",
                            style = MaterialTheme.typography.bodyLarge,
                            color = textSecondaryColor()
                        )
                    }
                    innerTextField()
                },
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f),
                cursorBrush = SolidColor(Primary),
                singleLine = true,

                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onAdd() })
            )
            IconButton(
                onClick = onAdd,
                enabled = value.isNotEmpty(),
                colors = IconButtonColors(
                    containerColor = Primary,
                    contentColor = backgroundPrimaryColor(),
                    disabledContainerColor = textSecondaryColor(),
                    disabledContentColor = backgroundPrimaryColor()
                ),

            ) {
                Icon(
                    imageVector =  Lucide.ArrowUp,
                    contentDescription = null
                )
            }
        }
    }

    if (showDatePicker) {
        CustomDatePickerDialog(
            date = date,
            onDateSelected = { onDateChange(it) },
            onDismiss = { showDatePicker = false }
        )
    }
}

@Preview
@Composable
private fun Preview() {
    var value by rememberSaveable { mutableStateOf("") }
    QuickAdd(
        value = value,
        date = null,
        latitude = null,
        longitude = null,
        onValueChange = {
            value = it
        },
        onAdd = {

        },
        onDateChange = {

        },
        onLocationChangeRequest = {

        }
    )
}