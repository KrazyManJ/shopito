package dev.krazymanj.shopito.ui.screens.mapLocationPicker

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krazymanj.shopito.model.Location
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MapLocationPickerViewModel @Inject constructor() : ViewModel(),
    MapLocationPickerActions {

    private val _state : MutableStateFlow<MapLocationPickerUIState> = MutableStateFlow(value = MapLocationPickerUIState())

    val state = _state.asStateFlow()

    override fun loadLocation(location: Location?) {
        _state.update { it.copy(
            location = location ?: it.location,
            isLoading = false
        ) }
    }

    override fun locationChanged(location: Location) {
        _state.value = _state.value.copy(
            location = location
        )
    }

    override fun reset() {
        _state.value = MapLocationPickerUIState()
    }
}