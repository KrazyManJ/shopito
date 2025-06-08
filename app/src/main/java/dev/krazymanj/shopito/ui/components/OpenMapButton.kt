package dev.krazymanj.shopito.ui.components

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.SquareArrowOutUpRight
import dev.krazymanj.shopito.viewmodel.SettingsAccessorViewModel

@Composable
fun OpenMapButton(
    latitude: Double,
    longitude: Double,
    startNavigation: Boolean = false
) {
    val viewModel = hiltViewModel<SettingsAccessorViewModel>()

    val state = viewModel.state.collectAsStateWithLifecycle()

    if (state.value.loading) {
        viewModel.load()
    }

    val context = LocalContext.current

    IconButton(onClick = {
        val uri = Uri.parse(
            if (state.value.startNavigationSetting)
                "google.navigation:q=$latitude,$longitude"
            else
                "geo:$latitude,$longitude?q=$latitude,$longitude"
        )
        val mapIntent = Intent(Intent.ACTION_VIEW, uri)
        mapIntent.setPackage("com.google.android.apps.maps")

        if (mapIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(mapIntent)
        } else {
            Toast.makeText(context, "Google Maps is not installed", Toast.LENGTH_SHORT).show()
        }
    }) {
        Icon(
            imageVector = Lucide.SquareArrowOutUpRight,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
    }
}
