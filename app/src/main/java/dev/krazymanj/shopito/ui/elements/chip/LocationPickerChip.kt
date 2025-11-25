package dev.krazymanj.shopito.ui.elements.chip

import androidx.compose.runtime.Composable
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MapPin
import dev.krazymanj.shopito.model.Location

@Composable
fun LocationPickerChip(
    location: Location?,
    onLocationChangeRequest: () -> Unit
) {
    PickerChip(
        selected = location != null,
        onClick = onLocationChangeRequest,
        leadingIcon = Lucide.MapPin,
        label = when {
            location != null -> "Selected"
            else -> "Location"
        }
    )
}