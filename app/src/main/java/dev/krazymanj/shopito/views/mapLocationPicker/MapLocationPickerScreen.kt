package dev.krazymanj.shopito.views.mapLocationPicker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.navigation.INavigationRouter
import dev.krazymanj.shopito.ui.components.BaseScreen
import dev.krazymanj.shopito.ui.theme.spacing16

@Composable
fun MapLocationPickerScreen(
    navRouter: INavigationRouter,
    latitude: Double?,
    longitude: Double?
) {
    val viewModel = hiltViewModel<MapLocationPickerViewModel>()

    val state = viewModel.templateUIState.collectAsStateWithLifecycle()

    LaunchedEffect(latitude,longitude) {
        if (latitude != null && longitude != null) {
            viewModel.locationChanged(latitude, longitude)
        }
    }

    BaseScreen(
        topBarText = stringResource(R.string.pick_location_title),
        navigationRouter = navRouter,
        onBackClick = {
            navRouter.returnBack()
        }
    ) {
        MapPositionPickerScreenContent(
            paddingValues = it,
            state = state.value,
            actions = viewModel,
            navRouter = navRouter
        )
    }
}

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun MapPositionPickerScreenContent(
    paddingValues: PaddingValues,
    state: MapLocationPickerUIState,
    actions: MapLocationPickerActions,
    navRouter: INavigationRouter
) {
    val mapUiSettings by remember { mutableStateOf(
        MapUiSettings(
            zoomControlsEnabled = false,
            mapToolbarEnabled = false
        )
    ) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(state.latitude,state.longitude), 0f)
    }

    Box(
        modifier = Modifier.fillMaxSize().padding(paddingValues)
    ) {
        GoogleMap(modifier = Modifier.fillMaxHeight(),
            uiSettings = mapUiSettings,
            cameraPositionState = cameraPositionState
        ) {
            MapEffect { map ->
                map.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener{
                    override fun onMarkerDrag(p0: Marker) {}
                    override fun onMarkerDragEnd(p0: Marker) {
                        actions.locationChanged(p0.position.latitude, p0.position.longitude)
                    }
                    override fun onMarkerDragStart(p0: Marker) {}
                })
            }

            Marker(
                state = MarkerState(position = LatLng(state.latitude,state.longitude)),
                draggable = true
            )
        }



        Button(
            modifier = Modifier
                .padding(
                    start = spacing16,
                    end = spacing16,
                    bottom = spacing16
                )
                .align(Alignment.BottomCenter),
            onClick = {
                navRouter.navigateBackWithLocationData(state.latitude, state.longitude)
            },
        ) {
            Text(text = stringResource(R.string.save_label))
        }
    }
}
