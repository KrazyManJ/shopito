package dev.krazymanj.shopito.ui.elements.chip

import android.location.Address
import android.location.Geocoder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MapPin
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.model.Location
import java.util.Locale

@Composable
fun LocationPickerChip(
    location: Location?,
    onLocationChangeRequest: () -> Unit,
    onLocationClearRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var address by remember { mutableStateOf<Address?>(null) }

    LaunchedEffect(location) {
        if (location != null) {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addressList = geocoder.getFromLocation(location.latitude, location.longitude,1)
            if (!addressList.isNullOrEmpty()) address = addressList[0]
        }
    }

    PickerChip(
        selected = location != null,
        onClick = onLocationChangeRequest,
        leadingIcon = Lucide.MapPin,
        label = when {
            location != null -> address?.getAddressLine(0) ?: stringResource(R.string.loading_label)
            else -> stringResource(R.string.location_label)
        },
        onXClick = onLocationClearRequest,
        modifier = modifier.then(Modifier)
    )
}