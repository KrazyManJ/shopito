package dev.krazymanj.shopito.ui.elements.chip

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.krazymanj.shopito.ui.theme.Primary
import dev.krazymanj.shopito.ui.theme.backgroundPrimaryColor
import dev.krazymanj.shopito.ui.theme.textPrimaryColor
import dev.krazymanj.shopito.ui.theme.textSecondaryColor

@Composable
private fun pickerChipColors(): SelectableChipColors{
    return SelectableChipColors(
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
}

@Composable
fun PickerChip(
    selected: Boolean,
    onClick: () -> Unit,
    leadingIcon: ImageVector,
    label: String
) {
    InputChip(
        selected = selected,
        onClick = onClick,
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        },
        label = {
            Text(text = label)
        },
        colors = pickerChipColors()
    )
}