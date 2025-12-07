package dev.krazymanj.shopito.ui.elements.input

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.ArrowUp
import com.composables.icons.lucide.Lucide
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.model.Location
import dev.krazymanj.shopito.ui.UITestTag
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
    onDateChange: (Long?) -> Unit,
    onLocationChangeRequest: () -> Unit,
    onLocationClearRequest: () -> Unit,

    onAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.then(Modifier
            .fillMaxWidth()
            .background(backgroundPrimaryColor())
            .padding(horizontal = spacing16, vertical = spacing8)
            .navigationBarsPadding())
            .testTag(UITestTag.QuickAdd.QuickAdd)
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
                onLocationChangeRequest = onLocationChangeRequest,
                onLocationClearRequest = onLocationClearRequest
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            BorderFreeTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = stringResource(R.string.quick_add_placeholder),
                modifier = Modifier.weight(1f).testTag(UITestTag.QuickAdd.TextField),
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
                modifier = Modifier.testTag(UITestTag.QuickAdd.AddButton)

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
        onLocationChangeRequest = {},
        onLocationClearRequest = {}
    )
}