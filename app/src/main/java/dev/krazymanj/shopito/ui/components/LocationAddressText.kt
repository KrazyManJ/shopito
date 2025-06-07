package dev.krazymanj.shopito.ui.components

import android.location.Address
import android.location.Geocoder
import android.location.Geocoder.GeocodeListener
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
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

    Text(
        text = when {
            loading -> "Loading..."
            address != null -> address!!.getAddressLine(0)
            else -> "Unspecified"
        }
    )
}