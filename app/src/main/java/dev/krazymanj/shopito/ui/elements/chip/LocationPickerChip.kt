package dev.krazymanj.shopito.ui.elements.chip

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MapPin
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.model.Location
import dev.krazymanj.shopito.viewmodel.GeoReverseViewModel

@Composable
fun LocationPickerChip(
    location: Location?,
    onLocationChangeRequest: () -> Unit,
    onLocationClearRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<GeoReverseViewModel>()

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(location) {
        if (location != null) {
            viewModel.reverse(location)
        }
    }

    Log.i("Test", "Location $location")

    PickerChip(
        selected = location != null,
        onClick = onLocationChangeRequest,
        leadingIcon = Lucide.MapPin,
        label = when {
            location == null -> stringResource(R.string.location_label)
            state.locationLabel != null -> state.locationLabel!!
            else -> stringResource(R.string.location_label)
        },
        showLoading = state.isLoading && location != null,
        onXClick = onLocationClearRequest,
        modifier = modifier.then(Modifier)
    )
}