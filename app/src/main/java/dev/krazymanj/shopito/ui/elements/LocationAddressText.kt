package dev.krazymanj.shopito.ui.elements

import android.location.Address
import android.location.Geocoder
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import dev.krazymanj.shopito.R
import java.util.Locale

@Composable
fun LocationAddressText(
    latitude: Double?,
    longitude: Double?
) {
    var address by remember { mutableStateOf<Address?>(null) }
    var loading by remember { mutableStateOf(true) }

    val context = LocalContext.current

    LaunchedEffect(latitude,longitude) {
        if (latitude != null && longitude != null) {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addressList = geocoder.getFromLocation(latitude,longitude,1)
            if (!addressList.isNullOrEmpty()) address = addressList[0]
        }
        loading = false
    }

    Row(
        Modifier.fillMaxWidth()
    ) {
        Text(
            text = when {
                loading -> stringResource(R.string.loading_label)
                address != null -> address!!.getAddressLine(0)
                else -> stringResource(R.string.unspecified_label)
            },
            modifier = Modifier.weight(1f)
        )
        address?.let {
            OpenMapButton(latitude!!,longitude!!)
        }
    }
}