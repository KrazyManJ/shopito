package dev.krazymanj.shopito.views.mapLocationPicker

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krazymanj.shopito.database.IShopitoLocalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MapLocationPickerViewModel @Inject constructor(private val repository: IShopitoLocalRepository) : ViewModel(),
    MapLocationPickerActions {

    private val _state : MutableStateFlow<MapLocationPickerUIState> = MutableStateFlow(value = MapLocationPickerUIState())

    val templateUIState = _state.asStateFlow()
    override fun locationChanged(latitude: Double, longitude: Double) {
        _state.value = _state.value.copy(
            latitude = latitude,
            longitude = longitude
        )
    }
}