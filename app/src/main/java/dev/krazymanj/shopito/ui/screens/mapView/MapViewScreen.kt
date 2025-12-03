package dev.krazymanj.shopito.ui.screens.mapView

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.ComposeMapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.rememberCameraPositionState
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.navigation.Destination
import dev.krazymanj.shopito.navigation.INavigationRouter
import dev.krazymanj.shopito.ui.elements.ShopitoNavigationBar
import dev.krazymanj.shopito.ui.elements.screen.BaseScreen

@Composable
fun MapViewScreen(
    navRouter: INavigationRouter
) {
    val viewModel = hiltViewModel<MapViewViewModel>()

    LaunchedEffect(Unit) {
        viewModel.loadLocations()
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    BaseScreen(
        topBarText = stringResource(R.string.map_view_title),
        showLoading = state.isLoading,
        bottomBar = { ShopitoNavigationBar(navigationRouter = navRouter) },
    ) {
        MapViewScreenContent(
            paddingValues = it,
            state = state,
            navRouter = navRouter
        )
    }
}

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun MapViewScreenContent(
    paddingValues: PaddingValues,
    state: MapViewUIState,
    navRouter: INavigationRouter
) {
    val uiSettings by remember { mutableStateOf(
        MapUiSettings(
            compassEnabled = true,
            zoomControlsEnabled = false,
            mapToolbarEnabled = false,
        )
    ) }

    val cameraPositionState = rememberCameraPositionState()

    Box(
        Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            uiSettings = uiSettings,
            mapColorScheme = ComposeMapColorScheme.FOLLOW_SYSTEM,
            onMapLoaded = {
                if (state.locations.isEmpty()) {
                    return@GoogleMap
                }
                val boundsBuilder = LatLngBounds.builder()
                state.locations.forEach { boundsBuilder.include(it.toLatLng()) }
                cameraPositionState.move(
                    CameraUpdateFactory.newLatLngBounds(
                        boundsBuilder.build(),
                        128
                    )
                )
            },
            cameraPositionState = cameraPositionState
        ) {
            Clustering(
                items = state.locations,
                onClusterItemClick = {
                    navRouter.navigateTo(Destination.LocationItemsList(it))
                    // TODO: On click, open list with items in that location
                    true
                }
            )
        }
    }

}