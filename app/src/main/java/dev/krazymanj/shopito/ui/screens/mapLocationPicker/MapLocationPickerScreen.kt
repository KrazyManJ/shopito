package dev.krazymanj.shopito.ui.screens.mapLocationPicker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Pin
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.ComposeMapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.rememberCameraPositionState
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.model.Location
import dev.krazymanj.shopito.navigation.Destination
import dev.krazymanj.shopito.navigation.INavigationRouter
import dev.krazymanj.shopito.ui.elements.screen.BaseScreen
import dev.krazymanj.shopito.ui.theme.Primary
import dev.krazymanj.shopito.ui.theme.spacing32
import dev.krazymanj.shopito.ui.theme.spacing8

@Composable
fun MapLocationPickerScreen(
    navRouter: INavigationRouter,
    route: Destination.MapLocationPickerScreen
) {
    val viewModel = hiltViewModel<MapLocationPickerViewModel>()

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(route.location) {
        viewModel.loadLocation(route.location)
    }

    BaseScreen(
        topBarText = stringResource(R.string.pick_location_title),
        showLoading = state.isLoading,
        onBackClick = {
            navRouter.returnBack()
        }
    ) {
        MapPositionPickerScreenContent(
            state = state,
            actions = viewModel,
            navRouter = navRouter,
            route = route
        )
    }
}

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun MapPositionPickerScreenContent(
    state: MapLocationPickerUIState,
    actions: MapLocationPickerActions,
    navRouter: INavigationRouter,
    route: Destination.MapLocationPickerScreen
) {
    val mapUiSettings by remember { mutableStateOf(
        MapUiSettings(
            zoomControlsEnabled = false,
            mapToolbarEnabled = false,
            compassEnabled = true
        )
    ) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(state.location.toLatLng(), 13f)
    }

    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            val target = cameraPositionState.position.target
            actions.locationChanged(
                Location(
                    latitude = target.latitude,
                    longitude = target.longitude
                )
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxHeight(),
            uiSettings = mapUiSettings,
            cameraPositionState = cameraPositionState,
            mapColorScheme = ComposeMapColorScheme.FOLLOW_SYSTEM
        ) {}

        Icon(
            imageVector = Lucide.Pin,
            contentDescription = null,
            tint = Primary,
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.Center)
                .offset(y = (-24).dp)
        )

        Button(
            modifier = Modifier
                .padding(spacing32)
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            onClick = {
                navRouter.setPreviousState(route.navSource, state.location)
                navRouter.returnBack()
                actions.reset()
            },
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = stringResource(R.string.save_label),
                modifier = Modifier.padding(spacing8),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
