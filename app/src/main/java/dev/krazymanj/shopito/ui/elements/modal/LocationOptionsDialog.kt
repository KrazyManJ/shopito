package dev.krazymanj.shopito.ui.elements.modal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MapPin
import com.composables.icons.lucide.X
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.model.Location
import dev.krazymanj.shopito.model.SavedLocation
import dev.krazymanj.shopito.ui.theme.Emphasized
import dev.krazymanj.shopito.ui.theme.Primary
import dev.krazymanj.shopito.ui.theme.backgroundPrimaryColor
import dev.krazymanj.shopito.ui.theme.spacing16
import dev.krazymanj.shopito.ui.theme.spacing4
import dev.krazymanj.shopito.ui.theme.spacing8
import dev.krazymanj.shopito.ui.theme.textPrimaryColor
import dev.krazymanj.shopito.ui.theme.textSecondaryColor

@Composable
fun LocationOptionsDialog(
    options: Set<SavedLocation>,
    selectedLocation: Location?,
    onDismissRequest: () -> Unit,
    onSelected: (SavedLocation) -> Unit,
    onMapPickerClicked: () -> Unit,
    onDeleteRequest: (SavedLocation) -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(
                containerColor = backgroundPrimaryColor(),
                contentColor = textPrimaryColor()
            )
        ) {
            Column(
                modifier = Modifier.padding(spacing16)
            ) {
                Text(
                    text = "Choose a location",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(horizontal = spacing16, vertical = spacing8),
                    fontWeight = FontWeight.Emphasized
                )
                Spacer(Modifier.height(spacing16))
                OptionItem(
                    showBottomBorder = options.isNotEmpty(),
                    onClick = {
                        onDismissRequest()
                        onMapPickerClicked()
                    },
                ) {
                    Icon(
                        imageVector = Lucide.MapPin,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(spacing8))
                    Text(
                        text = "Choose from a map...",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                if (options.isEmpty()) {
                    Text(
                        text = "No available options...",
                        color = textSecondaryColor(),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(spacing16)
                    )
                }
                options.reversed().forEachIndexed { index, savedLocation ->
                    OptionItem(
                        showBottomBorder = index < options.size-1,
                        onClick = {
                            onDismissRequest()
                            onSelected(savedLocation)
                        },
                        onXClick = {
                            onDeleteRequest(savedLocation)
                        }
                    ) {
                        Text(
                            text = savedLocation.label,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (savedLocation.location == selectedLocation) Primary else Color.Unspecified,
                            fontWeight = if (savedLocation.location == selectedLocation) FontWeight.Emphasized else null
                        )
                    }
                }
                Spacer(Modifier.height(spacing16))
                Row {
                    Spacer(Modifier.weight(1f))
                    Button(
                        onClick = {
                            onDismissRequest()
                        }
                    ) {
                        Text(text = stringResource(R.string.cancel_label))
                    }
                }
            }
        }
    }
}

@Composable
private fun OptionItem(
    showBottomBorder: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onXClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Row(
        modifier = modifier.then(Modifier
            .padding(vertical = spacing16, horizontal = spacing4)
            .clickable{ onClick() }
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            content()
        }
        onXClick?.let {
            IconButton(
                onClick = { it() }
            ) {
                Icon(
                    imageVector = Lucide.X,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
    if (showBottomBorder) {
        HorizontalDivider(
            color = textSecondaryColor(),
            thickness = 1.dp
        )
    }
}