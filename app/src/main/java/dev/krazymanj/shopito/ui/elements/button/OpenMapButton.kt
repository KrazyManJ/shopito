package dev.krazymanj.shopito.ui.elements.button

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Navigation
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.model.Location
import dev.krazymanj.shopito.viewmodel.SettingsAccessorViewModel

@Composable
fun OpenMapButton(
    location: Location
) {
    val viewModel = hiltViewModel<SettingsAccessorViewModel>()

    val state = viewModel.state.collectAsStateWithLifecycle()

    val lat = location.latitude
    val lon = location.longitude

    if (state.value.loading) {
        viewModel.load()
    }

    val context = LocalContext.current

    val label = stringResource(R.string.googlemaps_not_installed)

    IconButton(onClick = {
        val uri = if (state.value.startNavigationSetting)
            "google.navigation:q=$lat,$lon".toUri()
        else
            "geo:$lat,$lon?q=$lat,$lon".toUri()
        val mapIntent = Intent(Intent.ACTION_VIEW, uri)
        mapIntent.setPackage("com.google.android.apps.maps")

        if (mapIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(mapIntent)
        } else {
            Toast.makeText(context, label, Toast.LENGTH_SHORT).show()
        }
    }) {
        Icon(
            imageVector = Lucide.Navigation,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
    }
}
