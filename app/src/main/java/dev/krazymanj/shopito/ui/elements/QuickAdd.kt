package dev.krazymanj.shopito.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.ArrowUp
import com.composables.icons.lucide.Lucide
import dev.krazymanj.shopito.model.Location
import dev.krazymanj.shopito.ui.elements.chip.DatePickerChip
import dev.krazymanj.shopito.ui.elements.chip.LocationPickerChip
import dev.krazymanj.shopito.ui.theme.Primary
import dev.krazymanj.shopito.ui.theme.backgroundPrimaryColor
import dev.krazymanj.shopito.ui.theme.spacing16
import dev.krazymanj.shopito.ui.theme.spacing8
import dev.krazymanj.shopito.ui.theme.textSecondaryColor


@Composable
fun QuickAdd(
    value: String,
    date: Long?,
    location: Location?,

    onValueChange: (String) -> Unit,
    onDateChange: (Long) -> Unit,
    onLocationChangeRequest: () -> Unit,

    onAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
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
            DatePickerChip(
                date = date,
                onDateChange = onDateChange
            )
            LocationPickerChip(
                location = location,
                onLocationChangeRequest = onLocationChangeRequest
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
}

@Preview
@Composable
private fun Preview() {
    QuickAdd(
        value = "",
        date = null,
        location = null,
        onValueChange = {},
        onAdd = {},
        onDateChange = {},
        onLocationChangeRequest = {}
    )
}