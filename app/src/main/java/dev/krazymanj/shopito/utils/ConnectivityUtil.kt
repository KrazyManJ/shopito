package dev.krazymanj.shopito.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

enum class ConnectionState {
    Available, Unavailable
}

@Composable
fun rememberConnectivityState(): State<ConnectionState> {
    val context = LocalContext.current

    val connectivityState = remember { mutableStateOf(ConnectionState.Unavailable) }

    DisposableEffect(context) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                connectivityState.value = ConnectionState.Available
            }

            override fun onLost(network: Network) {
                connectivityState.value = ConnectionState.Unavailable
            }
        }

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, callback)

        val currentNetwork = connectivityManager.activeNetwork
        val caps = connectivityManager.getNetworkCapabilities(currentNetwork)
        val isConnected = caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        connectivityState.value = if (isConnected) ConnectionState.Available else ConnectionState.Unavailable

        onDispose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }

    return connectivityState
}