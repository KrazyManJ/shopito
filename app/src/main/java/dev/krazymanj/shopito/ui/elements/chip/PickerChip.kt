package dev.krazymanj.shopito.ui.elements.chip

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Image
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.X
import dev.krazymanj.shopito.ui.theme.Primary
import dev.krazymanj.shopito.ui.theme.backgroundPrimaryColor
import dev.krazymanj.shopito.ui.theme.textPrimaryColor
import dev.krazymanj.shopito.ui.theme.textSecondaryColor

@Composable
private fun pickerChipColors(): SelectableChipColors{
    return SelectableChipColors(
        containerColor = backgroundPrimaryColor(),
        labelColor = textPrimaryColor(),
        leadingIconColor = Primary,
        trailingIconColor = textPrimaryColor(),
        disabledContainerColor = backgroundPrimaryColor(),
        disabledLabelColor = textSecondaryColor(),
        disabledLeadingIconColor = textSecondaryColor(),
        disabledTrailingIconColor = textSecondaryColor(),
        selectedContainerColor = Primary,
        disabledSelectedContainerColor = textSecondaryColor(),
        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
        selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
        selectedTrailingIconColor = MaterialTheme.colorScheme.onPrimary
    )
}

@Composable
fun PickerChip(
    selected: Boolean,
    onClick: () -> Unit,
    leadingIcon: ImageVector,
    label: String,
    onXClick: () -> Unit,
    modifier: Modifier = Modifier,
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
            Text(
                text = label,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        trailingIcon = {
            if (selected) {
                Icon(
                    imageVector = Lucide.X,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp).clickable {
                        onXClick()
                    }
                )
            }
        },
        colors = pickerChipColors(),
        modifier = modifier.then(Modifier)
    )
}

@PreviewLightDark
@Composable
private fun Preview() {
    Column {
        PickerChip(true, {}, Lucide.Image, "Chip", {},)
        PickerChip(false, {}, Lucide.Image, "Chip", {},)
    }
}