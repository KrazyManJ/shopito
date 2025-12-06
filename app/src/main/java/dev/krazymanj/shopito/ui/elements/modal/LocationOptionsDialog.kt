package dev.krazymanj.shopito.ui.elements.modal

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MapPin
import com.composables.icons.lucide.X
import dev.krazymanj.shopito.model.Location
import dev.krazymanj.shopito.model.SavedLocation
import dev.krazymanj.shopito.ui.theme.Emphasized
import dev.krazymanj.shopito.ui.theme.Primary
import dev.krazymanj.shopito.ui.theme.spacing8
import dev.krazymanj.shopito.utils.ConnectionState
import dev.krazymanj.shopito.utils.rememberConnectivityState

sealed class LocationOptionRowType {
    data object MapPicker : LocationOptionRowType()
    data class Row(val savedLocation: SavedLocation) : LocationOptionRowType()
}

@Composable
fun LocationOptionsDialog(
    options: Set<SavedLocation>,
    selectedLocation: Location?,
    onDismissRequest: () -> Unit,
    onSelected: (SavedLocation) -> Unit,
    onMapPickerClicked: () -> Unit,
    onDeleteRequest: (SavedLocation) -> Unit
) {
    val connectionState by rememberConnectivityState()

    OptionSelectDialog(
        options = listOf(LocationOptionRowType.MapPicker).plus(
            options.reversed().map { LocationOptionRowType.Row(it) }
        ),
        title = "Choose a location",
        row = {
            when(it) {
                LocationOptionRowType.MapPicker -> {
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
                is LocationOptionRowType.Row -> {
                    val savedLocation = it.savedLocation
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
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
                        IconButton(
                            onClick = { onDeleteRequest(it.savedLocation) }
                        ) {
                            Icon(
                                imageVector = Lucide.X,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        onOptionSelect = {
            onDismissRequest()
            when(it) {
                LocationOptionRowType.MapPicker -> {
                    onMapPickerClicked()
                }
                is LocationOptionRowType.Row -> {
                    onSelected(it.savedLocation)
                }
            }
        },
        enabledOptionsPredicate = {
            when(it) {
                LocationOptionRowType.MapPicker -> connectionState == ConnectionState.Available
                else -> true
            }
        }
    )
}